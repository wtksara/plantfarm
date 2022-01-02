package wtksara.plantfarm.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import wtksara.plantfarm.security.ClientService;

@Configuration
@EnableWebSecurity
// Zmiana domyślnych konfiguracji zabezpieczeń
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private ClientService clientService;

    @Autowired
    private JWTTokenHelper jwtTokenHelper;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;

    @Override
    // Metoda odpowiedzialna za przeprowadzenie autentykacji oraz autoryzacji użytkownika
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(clientService).passwordEncoder(passwordEncoder());
    }

    @Bean
    @Override
    // Nadpisanie metody authenticationManagerBean(), tak aby zdefiniować @Bean dla AuthenticationManager
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    // Właczenie zabezpieczeń HTTP, poprzez rozszerzenie domyślnej konfiguracji
    protected void configure(HttpSecurity http) throws Exception {
        // Ustawienie sesji na bezstanowa, gdyż nie ma być ona wykorzystywana do przechowywania stanu użtkownika
        http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                // Obsługa wyjątków podczas uwierzytelniania użytkownika
                .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint).and()
                // Obsługa żądań
                .authorizeRequests((request) ->
                        // Żadania nie wymagające uwierzytelnienia użytkownika
                        request.antMatchers(
                                "/api/plants/show",
                                "/api/plants/*/download",
                                "/api/cultivation",
                                "/api/patches",
                                "/api/tank/*",
                                "/api/auth/login"
                        ).permitAll()
                                // Wszystkie inne wymagają
                        .antMatchers(HttpMethod.OPTIONS, "/**").permitAll().anyRequest().authenticated())
                // Filtr, sprawdzający token przed każdym żadaniem
                .addFilterBefore(new JWTAuthenticationFilter(clientService, jwtTokenHelper),
                        UsernamePasswordAuthenticationFilter.class);
        // Wyłaczenie usługi CSRF token, gdyz uwierzytelnienie jest przeprowadzane za pomocą tokenu na okaziciela,
        // a nie plików cookie.
        http.csrf().disable().headers().frameOptions().disable();
    }

    @Bean
    // Kodowanie hasła przy pomocy BCrypt
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
