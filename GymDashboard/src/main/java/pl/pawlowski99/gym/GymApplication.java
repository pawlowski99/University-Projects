package pl.pawlowski99.gym;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import pl.pawlowski99.gym.service.LoadDB;

@SpringBootApplication
public class GymApplication {

	public static void main(String[] args) {
		SpringApplication.run(GymApplication.class, args);
	}

	@Bean
	public CommandLineRunner setUpApp(@Autowired LoadDB loadDB){ return (args) -> loadDB.loadData();}
}
