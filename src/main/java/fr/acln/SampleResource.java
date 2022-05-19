package fr.acln;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.eclipse.microprofile.config.inject.ConfigProperty;

import static javax.ws.rs.core.MediaType.APPLICATION_JSON;

@Path("sample")
public class SampleResource {

	@Inject
	@ConfigProperty(name = "message")
	private String message;

	@GET
	public Response message() {
		Sample sample = Sample.builder()
			.name("Le Nom")
			.age(99)
			.build();

		return Response.ok(sample, APPLICATION_JSON).build();
	}

}
