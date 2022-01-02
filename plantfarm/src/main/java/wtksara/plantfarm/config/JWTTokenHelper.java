package wtksara.plantfarm.config;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Date;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

// Komponent ułatwiający prace z tokenem
@Component
public class JWTTokenHelper {

    // Zmienna przechowująca nazwe aplikacji
    @Value("${jwt.auth.app}")
    private String appName;

    // Zmienna przechowująca sekretny klucz
    @Value("${jwt.auth.secret_key}")
    private String secretKey;

    // Zmienna przechowująca czas życia tokenu
    @Value("${jwt.auth.expires_in}")
    private int expiresIn;

    // HMAC z użyciem SHA-256 wybrany jako sposób zakodowania tokenu
    private SignatureAlgorithm SIGNATURE_ALGORITHM = SignatureAlgorithm.HS256;

    // Metoda pozwalająca na uzyskanie wszystkich roszczeń zawartych w tokenie
    private Claims getAllClaimsFromToken(String token) {
        Claims claims;
        try {
            claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            claims = null;
        }
        return claims;
    }

    // Metoda uzyskująca nazwe użytkownika z tokenu
    public String getUsernameFromToken(String token) {
        String username;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            username = claims.getSubject();
        } catch (Exception e) {
            username = null;
        }
        return username;
    }
    // Metoda generująca token dla wskazanego użytkownika
    public String generateToken(String username) throws InvalidKeySpecException, NoSuchAlgorithmException {
        return Jwts.builder()
                .setIssuer( appName )
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(generateExpirationDate())
                .signWith( SIGNATURE_ALGORITHM, secretKey )
                .compact();
    }

    // Metoda generująca date zakończenia działania tokenu
    private Date generateExpirationDate() {
        return new Date(new Date().getTime() + expiresIn * 1000);
    }

    // Walidacja tokenu
    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getUsernameFromToken(token);
        return (
                username != null &&
                        // Sprawdzenie czy nazwa uzytkownika zgadza sie z nazwa w bazie
                        username.equals(userDetails.getUsername()) &&
                        // Oraz czy token jest nadal aktywny
                        !isTokenExpired(token)
        );
    }
    // Sprawdzenie czy token jest nadal aktywny
    public boolean isTokenExpired(String token) {
        Date expireDate=getExpirationDate(token);
        return expireDate.before(new Date());
    }

    // Metoda pozwalająca na pobranie daty zakonczenia działania tokenu
    private Date getExpirationDate(String token) {
        Date expireDate;
        try {
            final Claims claims = this.getAllClaimsFromToken(token);
            expireDate = claims.getExpiration();
        } catch (Exception e) {
            expireDate = null;
        }
        return expireDate;
    }

    // Metoda uzyskująca token z nagłówka http
    public String getToken( HttpServletRequest request ) {

        String authHeader = getAuthHeaderFromHeader( request );
        if ( authHeader != null && authHeader.startsWith("Bearer ")) {
            return authHeader.substring(7);
        }

        return null;
    }

    // Uzyskanie nagłówka http
    public String getAuthHeaderFromHeader( HttpServletRequest request ) {
        return request.getHeader("Authorization");
    }
}
