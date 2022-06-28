package org.hankyu.simpleboard_v1;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class SimpleboardV1Application {

	public static void main(String[] args) {
		SpringApplication.run(SimpleboardV1Application.class, args);
	}



}
