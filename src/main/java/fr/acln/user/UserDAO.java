package fr.acln.user;

import fr.acln.game.Game;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public Optional<User> add(User user) {
        try {
            userTransaction.begin();
            entityManager.persist(user);
            userTransaction.commit();

            return Optional.of(user);
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return Optional.empty();
        }
    }

    public Optional<User> get(String username) {
        try {
            userTransaction.begin();
            Optional<User> maybeUser = entityManager.createQuery(
                    """
                        SELECT user
                        FROM User user
                        WHERE user.username = :username
                        """, User.class
                )
                .setParameter("username", username)
                .getResultList()
                .stream()
                .findFirst();
            userTransaction.commit();

            return maybeUser;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return Optional.empty();
        }
    }

    public boolean update(User user) {
        try {
            userTransaction.begin();
            entityManager.merge(user);
            userTransaction.commit();

            return true;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return false;
        }
    }

    public boolean removeToken(String token) {
        try {
            userTransaction.begin();
            int result = entityManager.createQuery(
                    """
                        UPDATE User user
                        SET user.token = NULL
                        WHERE user.token = :token
                        """
                )
                .setParameter("token", token)
                .executeUpdate();
            userTransaction.commit();

            return result != 0;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return false;
        }
    }

    public boolean isTokenValid(String token) {
        try {
            System.out.println("TOKEN: " + token);
            userTransaction.begin();
            List<User> users = entityManager.createQuery(
                    """
                        SELECT user
                        FROM User user
                        WHERE user.token = :token
                        """, User.class
                )
                .setParameter("token", token)
                .getResultList();
            userTransaction.commit();

            return users.size() > 0;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return false;
        }
    }

    public List<User> getAll() {
        try {
            userTransaction.begin();
            List<User> users = entityManager.createQuery(
                    """
                        SELECT user
                        FROM User user
                        """, User.class
                )
                .getResultList();
            userTransaction.commit();

            return users;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return List.of();
        }
    }

}
