package fish.payara.demos.conference.api;

import fish.payara.demos.conference.entities.Session;
import fish.payara.demos.conference.services.SessionService;
import jakarta.annotation.security.RolesAllowed;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.Response;

import java.security.Principal;
import java.util.List;

/**
 *
 * @author fabio
 */
@Path("/register")
@RequestScoped
@RolesAllowed("Attendee")
public class RegistrationResource {
    
    @Inject
    SessionService sessionService;
    
    @Inject
    Principal principal;
    
    @POST
    @Path("/{sessionId}")
    public Response register(@PathParam("sessionId") Integer sessionId){
        Session session = sessionService.retrieve(sessionId).orElseThrow(() -> new NotFoundException("No session found"));
        return Response.ok(sessionService.register(session, principal.getName())).build();
    }
    
    @GET
    @Path("/current")
    public List<Session> currentSessions(){
        return sessionService.retrieveRegisteredSessions(principal.getName());
    }
}
