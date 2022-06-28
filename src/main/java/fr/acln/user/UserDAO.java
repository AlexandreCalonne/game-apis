package fr.acln.user;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;

public class UserDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public void add(User user) {
        entityManager.persist(user);
    }

    public Optional<User> get(String username) {
        return entityManager.createQuery(
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
    }

    public void update(User user) {
        entityManager.merge(user);
    }

    public boolean removeToken(String token) {
        int result = entityManager.createQuery(
                """
                    UPDATE User user
                    SET user.token = NULL
                    WHERE user.token = :token
                    """
            )
            .setParameter("token", token)
            .executeUpdate();

        return result != 0;
    }

    public Optional<User> getFromToken(String token) {
        return entityManager.createQuery(
                """
                    SELECT user
                    FROM User user
                    WHERE user.token = :token
                    """, User.class
            )
            .setParameter("token", token)
            .getResultList()
            .stream()
            .findFirst();
    }

    public List<User> getAll() {
        return entityManager.createQuery(
                """
                    SELECT user
                    FROM User user
                    """, User.class
            )
            .getResultList();
    }

}
