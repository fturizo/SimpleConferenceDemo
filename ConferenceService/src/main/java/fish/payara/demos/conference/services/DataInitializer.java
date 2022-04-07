package fish.payara.demos.conference.services;

import fish.payara.demos.conference.entities.Session;
import fish.payara.demos.conference.entities.Speaker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.Initialized;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;

import java.time.Duration;
import java.time.LocalDate;
import java.util.logging.Logger;

/**
 * @author Fabio Turizo
 */
@ApplicationScoped
public class DataInitializer {

    private static final Logger LOG = Logger.getLogger(DataInitializer.class.getName());
    
    @Inject
    SpeakerService speakerService;

    @Inject
    SessionService sessionService;
    
    public void initialize(@Observes @Initialized(ApplicationScoped.class) Object event){
        LOG.info("Initializing speakers");
        if(speakerService.getAll().isEmpty()){
            speakerService.save(new Speaker("Fabio Turizo", "Payara").accept());
            speakerService.save(new Speaker("Oleg Šelajev", "AtomicJar").accept());
        }
        LOG.info("Initializing sessions");
        if(sessionService.retrieveSessions().isEmpty()){
            sessionService.addNewSession(new Session("Easy Integration Testing with Payara and Testcontainers", "Virtual", LocalDate.now(), Duration.ofMinutes(45)));
        }
    }
}
