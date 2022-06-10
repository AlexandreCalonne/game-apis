package fr.acln.game;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class GameDAO {

    @PersistenceContext
    private EntityManager entityManager;

    public List<Game> getAll() {
        return entityManager.createQuery(
                """
                    SELECT game
                    FROM Game game
                    """, Game.class
            )
            .getResultList();
    }

    public Optional<Game> get(UUID id) {
        return entityManager.createQuery(
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
    }

    public boolean delete(UUID id) {
        int result = entityManager.createQuery(
                """
                    DELETE FROM Game game
                    WHERE game.id = :id
                    """
            )
            .setParameter("id", id)
            .executeUpdate();

        return result > 0;
    }

    public void add(Game game) {
        entityManager.persist(game);
    }

    public void update(Game game) {
        entityManager.merge(game);
    }

}
