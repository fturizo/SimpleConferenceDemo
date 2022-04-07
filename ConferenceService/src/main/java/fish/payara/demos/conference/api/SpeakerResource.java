package fish.payara.demos.conference.api;

import fish.payara.demos.conference.services.SpeakerService;
import fish.payara.demos.conference.entities.Speaker;
import jakarta.annotation.security.PermitAll;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import jakarta.ws.rs.core.Response.Status;

import java.util.List;
import java.util.Optional;

/**
 *
 * @author Fabio Turizo
 */
@Path("/speaker")
@RequestScoped
@Produces(MediaType.APPLICATION_JSON)
public class SpeakerResource {

    @Inject
    SpeakerService speakerService;

    @GET
    @Path("/{id}")
    public Response getSpeaker(@PathParam("id") Integer id) {
        return Optional.ofNullable(speakerService.get(id))
                .map(Response::ok)
                .orElse(Response.status(Status.NOT_FOUND))
                .build();
    }
    
    @GET
    @Path("/all")
    public List<Speaker> allSpeakers() {
        List<Speaker> all = speakerService.getAll();
        /*List<Speaker> all1 = speakerService.getAll();
        all.addAll(all1)*/;
        return all;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addSpeaker(Speaker speaker, @Context UriInfo uriInfo) {
        Speaker result = speakerService.save(speaker);
        return Response.created(UriBuilder.fromPath(uriInfo.getPath()).path("{id}").build(result.getId()))
                        .entity(speaker).build();
    }
    
    @POST
    @Path("/accept/{id}")
    @RolesAllowed("Admin")
    public Response acceptSpeaker(@PathParam("id") Integer id){
        Speaker speaker = speakerService.get(id).orElseThrow(() -> new NotFoundException("Speaker not found"));
        speakerService.save(speaker.accept());
        return Response.accepted().build();
    }
    
    @HEAD
    @PermitAll
    public Response checkSpeakers(@QueryParam("names") List<String> names){
        return (speakerService.allNamesExists(names) ? Response.ok() : Response.status(Status.NOT_FOUND)).build();
    }
}
