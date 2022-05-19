package fr.acln.game;

import javax.annotation.Resource;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.UserTransaction;
import java.util.List;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import static java.util.logging.Level.SEVERE;

public class GameDAO {

    @PersistenceContext
    private EntityManager entityManager;

    @Resource
    private UserTransaction userTransaction;

    public List<Game> getAll() {
        return entityManager.createQuery(
            """
                SELECT game
                FROM Game game
                """, Game.class
        ).getResultList();
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

}
