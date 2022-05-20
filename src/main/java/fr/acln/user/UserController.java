package fr.acln.user;

import fr.acln.security.BearerAuthentication;

import javax.ejb.EJB;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;
import static javax.ws.rs.core.Response.*;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Path("/users")
public class UserController {

    @EJB
    private UserService userService;

    @POST
    @Path("/register")
    @Produces(APPLICATION_JSON)
    public Response register(@HeaderParam("Authorization") String authorization) {
        return userService.register(authorization)
            .map(user -> ok(user).build())
            .orElse(serverError().build());
    }

    @POST
    @Path("/login")
    @Produces(APPLICATION_JSON)
    public Response login(@HeaderParam("Authorization") String basicAuthorization) {
        return userService.login(basicAuthorization)
            .map(token -> ok(token).build())
            .orElse(serverError().build());
    }

    @POST
    @Path("/logout")
    @Produces(APPLICATION_JSON)
    @BearerAuthentication
    public Response logout(@HeaderParam("Authorization") String bearerAuthorization) {
        System.out.println("LOGOUTTTTT");
        return userService.logout(bearerAuthorization) ? noContent().build() : status(BAD_REQUEST).build();
    }

    @GET
    @Produces(APPLICATION_JSON)
    public Response getAll() {
        return ok(userService.getAll()).build();
    }

}
