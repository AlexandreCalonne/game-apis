package fr.acln.exception;

import javax.persistence.EntityExistsException;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static fr.acln.exception.ExceptionUtils.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class EntityExistsMapper implements ExceptionMapper<EntityExistsException> {

    @Override
    public Response toResponse(EntityExistsException exception) {
        return status(BAD_REQUEST, exception.getMessage()).build();
    }

}
