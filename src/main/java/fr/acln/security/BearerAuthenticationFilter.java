package fr.acln.security;

import fr.acln.user.UserDAO;

import javax.annotation.Priority;
import javax.inject.Inject;
import javax.ws.rs.Priorities;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerRequestFilter;
import javax.ws.rs.core.HttpHeaders;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.Provider;

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

        if (isUnauthorized(authorizationHeader)) {
            abortWithUnauthorized(requestContext);
        }
    }

    private void abortWithUnauthorized(ContainerRequestContext requestContext) {
        requestContext.abortWith(Response.status(UNAUTHORIZED).build());
    }

    private boolean isUnauthorized(String authorizationHeader) {
        return !isRequestBearerAuthentication(authorizationHeader)
            || !userDAO.isTokenValid(extractTokenFromAuthorizationHeader(authorizationHeader));
    }

}
