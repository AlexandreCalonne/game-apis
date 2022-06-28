package fr.acln.user;

import fr.acln.game.Game;
import fr.acln.game.GameDAO;
import fr.acln.game.GameService;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Base64;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static com.google.common.hash.Hashing.sha256;
import static fr.acln.security.BearerUtils.extractTokenFromAuthorizationHeader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ejb.TransactionManagementType.BEAN;
import static org.mindrot.jbcrypt.BCrypt.*;

@Stateless
@TransactionManagement(BEAN)
public class UserService {

    public static final String BASIC = "Basic ";

    @Inject
    private UserDAO userDAO;

    @Inject
    private GameService gameService;

    @Transactional
    public void register(String authorization) {
        Credentials credentials = getDecodedCredentials(authorization);

        userDAO.add(User.builder()
            .username(credentials.username())
            .password(hashpw(credentials.password(), gensalt()))
            .build());
    }

    @Transactional
    public Optional<String> login(String basicAuthorization) {
        if (!basicAuthorization.startsWith(BASIC)) {
            return Optional.empty();
        }

        Credentials credentials = getDecodedCredentials(basicAuthorization);
        Optional<User> maybeUser = userDAO.get(credentials.username());

        if (maybeUser.isEmpty() || !checkpw(credentials.password(), maybeUser.get().getPassword())) {
            return Optional.empty();
        }

        User user = maybeUser.get();
        String token = generateToken(user);

        user.setToken(token);
        userDAO.update(user);

        return Optional.of(token);
    }

    @Transactional
    public boolean logout(String bearerAuthorization) {
        if (!bearerAuthorization.startsWith("Bearer ")) {
            return false;
        }

        return userDAO.removeToken(extractTokenFromAuthorizationHeader(bearerAuthorization));
    }

    @Transactional
    public boolean addLikedGame(String username, String gameId) {
        Optional<User> maybeUser = userDAO.get(username);
        Optional<Game> maybeGame = gameService.getById(gameId);

        if (maybeUser.isEmpty() || maybeGame.isEmpty()) {
            return false;
        }

        maybeUser.get().getGames().add(maybeGame.get());
        userDAO.update(maybeUser.get());

        return true;

    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

    public Optional<User> getByUsername(String username) {
        return userDAO.get(username);
    }

    private String generateToken(User user) {
        return sha256()
            .hashString(user.getUsername() + user.getPassword() + System.currentTimeMillis(), UTF_8)
            .toString();
    }

    private Credentials getDecodedCredentials(String authorization) {
        String[] splitCredentials = new String(
            Base64.getDecoder().decode(authorization.replace(BASIC, ""))
        ).split(":");

        return new Credentials(splitCredentials[0], splitCredentials[1]);
    }

}
