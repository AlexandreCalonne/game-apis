package fr.acln.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import static fr.acln.exception.ExceptionUtils.status;
import static javax.ws.rs.core.Response.Status.BAD_REQUEST;

@Provider
public class IllegalArgumentMapper implements ExceptionMapper<IllegalArgumentException> {

    @Override
    public Response toResponse(IllegalArgumentException exception) {
        return status(BAD_REQUEST, exception.getMessage()).build();
    }

}
