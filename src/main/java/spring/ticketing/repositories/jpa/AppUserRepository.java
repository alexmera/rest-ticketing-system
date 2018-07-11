package spring.ticketing.repositories.jpa;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import spring.ticketing.model.AppUserRol;
import spring.ticketing.model.jpa.AppUserJpa;

public interface AppUserRepository extends JpaRepository<AppUserJpa, Integer> {

  List<AppUserJpa> findByRol(AppUserRol rol);

  AppUserJpa findByUserName(String userName);
}
