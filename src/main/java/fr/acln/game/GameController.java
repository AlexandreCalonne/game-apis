package fr.acln.game;

import fr.acln.security.BearerAuthentication;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;
import static org.codehaus.plexus.util.StringUtils.isNotBlank;

@Path("/games")
@BearerAuthentication
public class GameController {

    @EJB
    private GameService gameService;

    @GET
    @Produces(APPLICATION_JSON)
    public Response get(@QueryParam("name") String name) {
        if (isNotBlank(name)) {
            return ok(gameService.getByName(name), APPLICATION_JSON).build();
        } else {
            return ok(gameService.getAll(), APPLICATION_JSON).build();
        }
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
        return gameService.add(game)
            .map(addedGame -> ok(addedGame, APPLICATION_JSON).build())
            .orElse(serverError().build());
    }

    @DELETE
    @Path("/{id}")
    @Produces(APPLICATION_JSON)
    public Response delete(@PathParam("id") String id) {
        return gameService.delete(id) ? noContent().build() : status(BAD_REQUEST).build();
    }

    @PUT
    @Path("/{id}")
    @Consumes(APPLICATION_JSON)
    @Produces(APPLICATION_JSON)
    public Response update(@PathParam("id") String id, Game game) {
        return gameService.update(id, game) ? noContent().build() : status(BAD_REQUEST).build();
    }

}
