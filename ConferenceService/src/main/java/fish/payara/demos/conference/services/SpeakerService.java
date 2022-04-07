package fish.payara.demos.conference.services;

import fish.payara.demos.conference.entities.Speaker;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author Fabio Turizo
 */
@ApplicationScoped
public class SpeakerService {
    
    @PersistenceContext(unitName = "Conference")
    EntityManager em;
    
    @Transactional
    public Speaker save(Speaker speaker){
        if(speaker.getId() == null){
            em.persist(speaker);
        }else{
            em.merge(speaker);
        }
        em.flush();
        return speaker;
    }
    
    public Optional<Speaker> get(Integer id){
        return Optional.ofNullable(em.find(Speaker.class, id));
    }
    
    public List<Speaker> getAll(){
        return em.createNamedQuery("Speaker.all", Speaker.class)
                 .getResultList();
    }
    
    public boolean allNamesExists(List<String> names){
        List<String> allNames = getAll().stream().map(Speaker::getName).collect(Collectors.toList());
        return names.stream().allMatch(allNames::contains);
    }
}
