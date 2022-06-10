package fr.acln.game;

import fr.acln.security.BearerAuthentication;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static javax.ws.rs.core.Response.*;
import static org.codehaus.plexus.util.StringUtils.isNotBlank;

@Path("/games")
@BearerAuthentication
public class GameController {

    @EJB
    private GameService gameService;

    @GET
    @Produces(APPLICATION_JSON)
    public Response get(@QueryParam("name") String name,
                        @QueryParam("platform") String platform) {
        List<Game> games = gameService.getAll();

        if (isNotBlank(name)) {
            games.retainAll(gameService.filterByName(games, name));
        }

        if (isNotBlank(platform)) {
            games.retainAll(gameService.filterByPlatform(games, platform));
        }

        return ok(games).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{id}")
    public Response getById(@PathParam("id") String id) {
        return gameService.getById(id)
            .map(game -> ok(game, APPLICATION_JSON).build())
            .orElse(status(NOT_FOUND).build());
    }

    @POST
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response add(Game game) {
        gameService.add(game);

        return ok().build();
    }

    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        return gameService.delete(id) ? noContent().build() : status(NOT_FOUND).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Game game) {
        gameService.update(id, game);

        return noContent().build();
    }

}
