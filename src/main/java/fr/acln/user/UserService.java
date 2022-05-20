package fr.acln.user;

import com.google.common.hash.Hashing;
import fr.acln.security.BearerUtils;
import org.mindrot.jbcrypt.BCrypt;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;

import java.nio.charset.StandardCharsets;
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

    public Optional<User> register(String authorization) {
        String[] credentials = getDecodedCredentials(authorization);

        return userDAO.add(User.builder()
            .username(credentials[0])
            .password(hashpw(credentials[1], gensalt()))
            .build());
    }

    public Optional<String> login(String basicAuthorization) {
        if (!basicAuthorization.startsWith("Basic ")) {
            return Optional.empty();
        }

        String[] credentials = getDecodedCredentials(basicAuthorization);
        Optional<User> maybeUser = userDAO.get(credentials[0]);

        if (maybeUser.isEmpty() || !checkpw(credentials[1], maybeUser.get().getPassword())) {
            return Optional.empty();
        }

        User user = maybeUser.get();
        user.setToken(generateToken(user));
        if (!userDAO.update(user)) {
            return Optional.empty();
        }

        return Optional.of(user.getToken());
    }

    public List<User> getAll() {
        return userDAO.getAll();
    }

    private String generateToken(User user) {
        return sha256().hashString(user.getUsername() + user.getPassword(), UTF_8).toString();
    }

    public boolean logout(String bearerAuthorization) {
        if (!bearerAuthorization.startsWith("Bearer ")) {
            return false;
        }

        return userDAO.removeToken(extractTokenFromAuthorizationHeader(bearerAuthorization));
    }

    private String[] getDecodedCredentials(String authorization) {
        return new String(
            Base64.getDecoder().decode(authorization.replace("Basic ", ""))
        ).split(":");
    }

}
