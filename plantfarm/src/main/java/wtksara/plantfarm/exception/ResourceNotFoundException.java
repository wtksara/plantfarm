package wtksara.plantfarm.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// Określenie statusu odpowiedzi metody kontrolera
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class ResourceNotFoundException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    // Treść wiadomości
    public ResourceNotFoundException(String message){
        super(message);
    }
}
