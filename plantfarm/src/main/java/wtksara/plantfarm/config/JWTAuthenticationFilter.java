package wtksara.plantfarm.config;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetails;
import org.springframework.web.filter.OncePerRequestFilter;
import wtksara.plantfarm.security.Client;
import wtksara.plantfarm.security.ClientService;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JWTAuthenticationFilter extends OncePerRequestFilter {

    private ClientService clientService;

    private JWTTokenHelper jwtTokenHelper;

    public JWTAuthenticationFilter(ClientService clientService, JWTTokenHelper jwtTokenHelper){
        this.clientService = clientService;
        this.jwtTokenHelper = jwtTokenHelper;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authToken = jwtTokenHelper.getToken(request);
        if (authToken != null){
            String username = jwtTokenHelper.getUsernameFromToken(authToken);
            if (username != null) {
                Client client = clientService.loadUserByUsername(username);
                if (jwtTokenHelper.validateToken(authToken,client)){
                    UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(client,null, client.getAuthorities());
                    authentication.setDetails(new WebAuthenticationDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authentication);
                }
            }
        }
        filterChain.doFilter(request,response);
    }
}
