package fr.acln.game;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.Optional;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.ok;
import static javax.ws.rs.core.Response.serverError;

@Path("/games")
public class GameController {

    @Inject
    private GameService gameService;

    @GET
    public Response getAll() {
        return ok(gameService.getAll(), APPLICATION_JSON).build();
    }

    @POST
    @Consumes(APPLICATION_JSON)
    public Response add(Game game) {
        return gameService.add(game)
            .map(addedGame -> ok(addedGame, APPLICATION_JSON).build())
            .orElse(serverError().build());
    }

}
