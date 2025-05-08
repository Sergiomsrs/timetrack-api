package org.tfg.timetrackapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class TimetrackApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(TimetrackApiApplication.class, args);
	}

}
