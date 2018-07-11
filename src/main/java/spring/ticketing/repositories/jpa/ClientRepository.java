package spring.ticketing.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ticketing.model.jpa.ClientJpa;

public interface ClientRepository extends JpaRepository<ClientJpa, Integer> {

}
