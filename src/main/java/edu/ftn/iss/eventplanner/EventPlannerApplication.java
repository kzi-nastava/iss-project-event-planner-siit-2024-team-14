package edu.ftn.iss.eventplanner;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class EventPlannerApplication {

	public static void main(String[] args) { SpringApplication.run(EventPlannerApplication.class, args); }

	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}
}
