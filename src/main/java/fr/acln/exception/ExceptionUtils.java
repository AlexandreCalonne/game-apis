package fr.acln.exception;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.Response.Status;

public class ExceptionUtils {

    public static ResponseBuilder status(Status status, String reason) {
        return Response.status(status.getStatusCode(), reason);
    }

}
