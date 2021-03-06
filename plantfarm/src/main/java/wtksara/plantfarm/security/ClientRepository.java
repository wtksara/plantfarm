package wtksara.plantfarm.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repozytorium dla encji Client
@Repository
public interface ClientRepository extends JpaRepository <Client, Long> {

    Client findByUsername(String username);
}
