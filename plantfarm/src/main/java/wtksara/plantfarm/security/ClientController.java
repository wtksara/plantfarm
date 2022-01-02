package wtksara.plantfarm.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import wtksara.plantfarm.config.JWTTokenHelper;
import wtksara.plantfarm.response.LoginResponse;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

// Pozwolenie na wysyłanie żądań cross-origin dla adresu http://localhost:3000/
@CrossOrigin(origins = "http://localhost:3000/")

// Komponent pełni role kontrolera
@RestController

// Mapowanie na poziomie klasy
@RequestMapping("/api/")
public class ClientController {


    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    JWTTokenHelper jWTTokenHelper;

    @PostMapping("/auth/login")
    // Metoda odpowiedzialna za przeprowadzenie procesu logowania
    public ResponseEntity<?> login(@RequestBody AuthenticationRequest authenticationRequest) throws InvalidKeySpecException, NoSuchAlgorithmException {

        // Uwierzytelnienie użytkownika
        final Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(), authenticationRequest.getPassword()));
         Client client = (Client) authentication.getPrincipal();
        // Wygenerowanie tokenu
        String jwtToken = jWTTokenHelper.generateToken(client.getUsername());
        LoginResponse response = new LoginResponse();
        response.setToken(jwtToken);
        // Zwrócenie tokenu
        return ResponseEntity.ok(response);
    }
}
