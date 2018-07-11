package spring.ticketing.repositories;

import spring.ticketing.model.AppUser;

public interface AppUserDao extends AppDao<Integer, AppUser> {

  AppUser create(AppUser entity, String rawPassword);

  @Override
  default AppUser create(AppUser entity) {
    throw new UnsupportedOperationException(
        "Operación no soportada para app_user, usar la sobrecarga"
    );
  }
}
