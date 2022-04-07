package fish.payara.demos.conference.entities;

import jakarta.json.bind.annotation.JsonbCreator;
import jakarta.json.bind.annotation.JsonbProperty;
import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * @author Fabio Turizo
 */
@Entity
@NamedQuery(name = "Speaker.all", query = "select sp from Speaker sp order by sp.id")
public class Speaker implements Serializable{
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    
    private String name;
    private String organization;
    private Boolean accepted;

    public Speaker() {
    }

    @JsonbCreator
    public Speaker(@JsonbProperty("name") String name, @JsonbProperty("organization") String organization) {
        this.name = name;
        this.organization = organization;
        this.accepted = false;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getOrganization() {
        return organization;
    }

    public boolean isAccepted() {
        return accepted;
    }
    
    public Speaker accept(){
        this.accepted = true;
        return this;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 79 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Speaker other = (Speaker) obj;
        return Objects.equals(this.id, other.id);
    }
}
