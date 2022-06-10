package fr.acln.user;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import javax.transaction.Transactional;
import java.util.Base64;
import java.util.List;
import java.util.Optional;

import static com.google.common.hash.Hashing.sha256;
import static fr.acln.security.BearerUtils.extractTokenFromAuthorizationHeader;
import static java.nio.charset.StandardCharsets.UTF_8;
import static javax.ejb.TransactionManagementType.BEAN;
import static org.mindrot.jbcrypt.BCrypt.*;

@Stateless
@TransactionManagement(BEAN)
public class UserService {

    @Inject
    private UserDAO userDAO;

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
        if (!basicAuthorization.startsWith("Basic ")) {
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

    public List<User> getAll() {
        return userDAO.getAll();
    }

    private String generateToken(User user) {
        return sha256()
            .hashString(user.getUsername() + user.getPassword(), UTF_8)
            .toString();
    }

    private Credentials getDecodedCredentials(String authorization) {
        String[] splitCredentials = new String(
            Base64.getDecoder().decode(authorization.replace("Basic ", ""))
        ).split(":");

        return new Credentials(splitCredentials[0], splitCredentials[1]);
    }

}
