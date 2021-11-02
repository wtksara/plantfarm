package wtksara.plantfarm;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;
import wtksara.plantfarm.security.Authority;
import wtksara.plantfarm.security.Client;
import wtksara.plantfarm.security.ClientRepository;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@SpringBootApplication
public class PlantfarmApplication {

	@Autowired
	private PasswordEncoder passwordEncoder;

	@Autowired
	private ClientRepository userRepository;

	public static void main(String[] args) {
		SpringApplication.run(PlantfarmApplication.class, args);
	}

	@PostConstruct
	protected void init(){
		List<Authority> authorityList = new ArrayList<>();
		authorityList.add(createAuthority("ADMIN"));
		Client user = new Client();
		user.setUsername("admin");
		user.setPassword(passwordEncoder.encode(("admin")));
		user.setEnabled(true);
		userRepository.save(user);
	}

	private Authority createAuthority (String role){
		Authority authority = new Authority();
		authority.setRole(role);
		return authority;
	}

}
