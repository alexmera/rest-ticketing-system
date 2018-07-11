package spring.ticketing;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import spring.ticketing.model.jpa.AppUserJpa;
import spring.ticketing.repositories.jpa.AppUserRepository;

@EntityScan(basePackageClasses = {AppUserJpa.class})
@EnableJpaRepositories(basePackageClasses = {AppUserRepository.class})
@SpringBootApplication
public class RestTicketingSystemApplication {

  public static void main(String[] args) {
    SpringApplication.run(RestTicketingSystemApplication.class, args);
  }
}
