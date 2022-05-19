package fr.acln.game;

import javax.ejb.Stateless;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.inject.Inject;

import java.util.List;
import java.util.Optional;

import static javax.ejb.TransactionManagementType.BEAN;

@Stateless
@TransactionManagement(BEAN)
public class GameService {

    @Inject
    private GameDAO gameDAO;

    public List<Game> getAll() {
        return gameDAO.getAll();
    }

    public Optional<Game> add(Game game) {
        return gameDAO.add(game);
    }

}
