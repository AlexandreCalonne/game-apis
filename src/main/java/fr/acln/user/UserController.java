package fr.acln.user;

import fr.acln.security.BearerAuthentication;

import javax.ejb.EJB;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.NOT_FOUND;

@Path("/users")
public class UserController {

    @EJB
    private UserService userService;

    @POST
    @Path("/register")
    @Produces(APPLICATION_JSON)
    public Response register(@HeaderParam("Authorization") String authorization) {
        userService.register(authorization);

        return noContent().build();
    }

    @POST
    @Path("/login")
    @Produces(APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String basicAuthorization) {
        return userService.login(basicAuthorization)
            .map(token -> ok(token).build())
            .orElse(status(BAD_REQUEST).build());
    }

    @POST
    @Path("/logout")
    @Produces(APPLICATION_JSON)
    @BearerAuthentication
    public Response logout(@HeaderParam("Authorization") String bearerAuthorization) {
        return userService.logout(bearerAuthorization) ? noContent().build() : status(BAD_REQUEST).build();
    }

    @POST
    @Path("/games/{gameId}")
    @BearerAuthentication
    public Response addLikedGame(@HeaderParam("username") String username,  @PathParam("gameId") String gameId) {
        userService.addLikedGame(username, gameId);

        if (!userService.addLikedGame(username, gameId)) {
            return status(NOT_FOUND).build();
        }

        return noContent().build();

    }

    @GET
    @Produces(APPLICATION_JSON)
    @BearerAuthentication
    public Response getAll() {
        return ok(userService.getAll()).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    @Path("/{username}")
    @BearerAuthentication
    public Response getByUsername(@PathParam("username") String username) {
        return userService.getByUsername(username)
            .map(user -> ok(user, APPLICATION_JSON).build())
            .orElse(status(NOT_FOUND).build());
    }

}
