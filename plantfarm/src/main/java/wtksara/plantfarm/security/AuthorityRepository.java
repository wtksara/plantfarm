package wtksara.plantfarm.security;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

// Repozytorium dla encji Authority
@Repository
public interface AuthorityRepository extends JpaRepository<Authority, Long> {
}
