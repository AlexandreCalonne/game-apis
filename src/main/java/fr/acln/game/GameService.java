package fr.acln.game;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static java.util.Arrays.stream;
import static javax.ejb.TransactionManagementType.BEAN;

@Stateless
@TransactionManagement(BEAN)
public class GameService {

    @Inject
    private GameDAO gameDAO;

    public List<Game> getAll() {
        return gameDAO.getAll();
    }

    public Optional<Game> getById(String id) {
        return gameDAO.get(UUID.fromString(id));
    }

    public List<Game> getByName(String name) {
        return gameDAO.getAll().stream()
            .filter(game -> countMatchingWords(name, game) > 0)
            .sorted((game1, game2) -> countMatchingWords(name, game2) - countMatchingWords(name, game1))
            .toList();
    }

    public boolean delete(String id) {
        return gameDAO.delete(UUID.fromString(id));
    }

    public Optional<Game> add(Game game) {
        return gameDAO.add(game);
    }

    public boolean update(String id, Game game) {
        game.setId(UUID.fromString(id));

        return gameDAO.update(game);
    }

    private int countMatchingWords(String searchInput, Game game) {
        return stream(searchInput.split(" "))
            .map(word -> game.getName().toUpperCase().contains(word.toUpperCase()) ? 1 : 0)
            .reduce(0, Integer::sum);
    }

}
