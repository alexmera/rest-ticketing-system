package spring.ticketing.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import spring.ticketing.model.jpa.TicketJpa;

public interface TicketRepository extends JpaRepository<TicketJpa, Integer> {

}
