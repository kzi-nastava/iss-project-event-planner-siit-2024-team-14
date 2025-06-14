package edu.ftn.iss.eventplanner;

import edu.ftn.iss.eventplanner.dtos.budget.SolutionItemDTO;
import edu.ftn.iss.eventplanner.entities.Product;
import edu.ftn.iss.eventplanner.entities.Service;
import edu.ftn.iss.eventplanner.entities.Solution;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableScheduling
public class EventPlannerApplication {

	public static void main(String[] args) { SpringApplication.run(EventPlannerApplication.class, args); }

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper mapper = new ModelMapper();
		mapper.getConfiguration().setSkipNullEnabled(true);

		mapper.createTypeMap(Service.class, SolutionItemDTO.class)
				.addMapping(s -> "service", SolutionItemDTO::setType);

		mapper.createTypeMap(Product.class, SolutionItemDTO.class)
				.addMapping(p -> "product", SolutionItemDTO::setType);

		return mapper;
	}
}
