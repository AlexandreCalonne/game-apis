package fr.acln.game;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class GameDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public List<Game> getAll() {
        try {
            userTransaction.begin();
            List<Game> games = entityManager.createQuery(
                    """
                        SELECT game
                        FROM Game game
                        """, Game.class
                )
                .getResultList();
            userTransaction.commit();

            return games;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return List.of();
        }
    }

    public Optional<Game> get(UUID id) {
        try {
            userTransaction.begin();
            Optional<Game> maybeGame = entityManager.createQuery(
                    """
                        SELECT game
                        FROM Game game
                        WHERE game.id = :id
                        """, Game.class
                )
                .setParameter("id", id)
                .getResultList()
                .stream()
                .findFirst();
            userTransaction.commit();

            return maybeGame;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return Optional.empty();
        }
    }

    public boolean delete(UUID id) {
        try {
            userTransaction.begin();
            int result = entityManager.createQuery(
                    """
                        DELETE FROM Game game
                        WHERE game.id = :id
                        """
                )
                .setParameter("id", id)
                .executeUpdate();
            userTransaction.commit();

            return result != 0;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return false;
        }
    }

    public Optional<Game> add(Game game) {
        try {
            userTransaction.begin();
            entityManager.persist(game);
            userTransaction.commit();

            return Optional.of(game);
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return Optional.empty();
        }
    }

    public boolean update(Game game) {
        try {
            userTransaction.begin();
            entityManager.merge(game);
            userTransaction.commit();

            return true;
        } catch (Exception e) {
            Logger.getGlobal().log(SEVERE, "JPA error: " + e.getMessage());

            return false;
        }
    }

}
