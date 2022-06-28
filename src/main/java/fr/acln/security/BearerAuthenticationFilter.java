package fr.acln.security;

import fr.acln.user.User;
import fr.acln.user.UserDAO;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.swing.text.html.Option;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

import java.util.Optional;

import static fr.acln.security.BearerUtils.extractTokenFromAuthorizationHeader;
import static fr.acln.security.BearerUtils.isRequestBearerAuthentication;
import static javax.ws.rs.core.HttpHeaders.AUTHORIZATION;
import static javax.ws.rs.core.Response.Status.UNAUTHORIZED;

@BearerAuthentication
@Provider
@Priority(Priorities.AUTHENTICATION)
public class BearerAuthenticationFilter implements ContainerRequestFilter {

    @Inject
    private UserDAO userDAO;

    @Override
    public void filter(ContainerRequestContext requestContext) {
        String authorizationHeader = requestContext.getHeaderString(AUTHORIZATION);

        Optional<User> maybeUser = getUserFromToken(authorizationHeader);

        if (maybeUser.isPresent()) {
            requestContext.getHeaders().add("username", maybeUser.get().getUsername());
        } else {
            abortWithUnauthorized(requestContext);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }

    private Optional<User> getUserFromToken(String authorizationHeader) {
        if (isRequestBearerAuthentication(authorizationHeader)) {
            return userDAO.getFromToken(extractTokenFromAuthorizationHeader(authorizationHeader));
        }

        return Optional.empty();
    }

}
