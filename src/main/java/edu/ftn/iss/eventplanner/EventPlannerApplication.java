package edu.ftn.iss.eventplanner;

import edu.ftn.iss.eventplanner.dtos.budget.SolutionItemDTO;
import edu.ftn.iss.eventplanner.dtos.getUsers.ProviderDTO;
import edu.ftn.iss.eventplanner.dtos.getUsers.UserDTO;
import edu.ftn.iss.eventplanner.entities.*;
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

		mapper.createTypeMap(User.class, UserDTO.class)
				.addMapping(ak -> "User", UserDTO::setRole);

		mapper.createTypeMap(Admin.class, UserDTO.class)
				.addMapping(a -> "Admin", UserDTO::setRole);

		mapper.createTypeMap(EventOrganizer.class, UserDTO.class)
				.addMapping(eo -> "EventOrganizer", UserDTO::setRole);

		mapper.createTypeMap(ServiceAndProductProvider.class, UserDTO.class)
				.addMapping(pup -> "ServiceAndProductProvider", UserDTO::setRole);

		mapper.createTypeMap(ServiceAndProductProvider.class, ProviderDTO.class)
				.addMapping(pup -> "ServiceAndProductProvider", ProviderDTO::setRole);

		return mapper;
	}


}
