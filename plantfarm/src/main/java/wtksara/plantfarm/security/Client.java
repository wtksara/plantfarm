package wtksara.plantfarm.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

// Encja przechowujaca informacje o kliencie implementujaca interface UserDetails
@Entity
@Table(name = "client")
public class Client implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Zmienna przechowujaca nazwe użytkownika
    @Column(name ="username")
    private String username;

    // Zmienna przechowujaca zakodowane hasło
    @Column(name ="password")
    private String password;

    // Zmienna przechowująca aktualny stan konta
    @Column(name = "enabled")
    private Boolean enabled;

    // Realizacja relacji M do N
    @ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    // Utworzenie nowej tabeli złaczeń
    @JoinTable(name="authorities",
            joinColumns = @JoinColumn(referencedColumnName = "id"),
            inverseJoinColumns = @JoinColumn(referencedColumnName ="id"))
    // Lista uprawnień dla danego klienta
    private List<Authority> authorities;

    public void setAuthorities(List<Authority> authorities) {
        this.authorities = authorities;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public boolean isEnabled() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonExpired() {
        return this.enabled;
    }

    @Override
    public boolean isAccountNonLocked() {
        return this.enabled;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return this.enabled;
    }


}
