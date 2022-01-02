package wtksara.plantfarm.security;

import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.util.Set;

// Encja przechowujaca informacje o uprawnieniach implementujaca interface GrantedAuthority
@Table(name="authority")
@Entity
public class Authority implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role")
    // Zmienna przechowująca nazwe roli
    private String role;

    @ManyToMany(mappedBy = "authorities")
    // Lista osób posiadajaca dane uprawnienie
    Set<Client> clients;

    @Override
    public String getAuthority() {
        return role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

}
