package wtksara.plantfarm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

// Komponent pełni role serwisu
@Service
public class ClientService implements UserDetailsService {

    @Autowired
    ClientRepository clientRepository;

    @Override
    // Metoda sprawdzająca czy dany użytkownik istnieje w bazie
    public Client loadUserByUsername(String username) throws UsernameNotFoundException {
        Client client = clientRepository.findByUsername(username);
        if (client==null){
            throw new UsernameNotFoundException("User not found with username" + username);
        }
        return client;
    }
}
