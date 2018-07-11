package spring.ticketing.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;
import spring.ticketing.model.jpa.AppUserJpa;
import spring.ticketing.repositories.AppUserDao;
import spring.ticketing.repositories.jpa.AppUserRepository;

@Service
public class AppUserSpringService implements AppUserService {

  private final AppUserDao appUserDao;
  private final AppUserRepository appUserRepository;

  public AppUserSpringService(AppUserDao appUserDao, AppUserRepository appUserRepository) {
    this.appUserDao = appUserDao;
    this.appUserRepository = appUserRepository;
  }

  @Nonnull
  @Override
  public List<AppUser> allUsers() {
    return appUserRepository.findAll()
        .stream()
        .map(AppUser.class::cast)
        .collect(Collectors.toList());
  }

  @Nonnull
  @Override
  public Optional<AppUser> findUserById(Integer id) {
    return appUserRepository.findById(id).map(AppUser.class::cast);
  }

  @Nonnull
  @Override
  @Transactional
  public AppUser createUser(AppUser appUser, String password) {
    return appUserDao.create(appUser, password);
  }

  @Nonnull
  @Override
  @Transactional
  public AppUser updateUser(AppUser appUser) {
    return appUserRepository.saveAndFlush(AppUserJpa.from(appUser));
  }

  @Nonnull
  @Override
  @Transactional
  public AppUser deleteUser(Integer id) {
    AppUserJpa appUser = appUserRepository.getOne(id);
    appUserRepository.deleteById(id);
    return appUser;
  }

  @Nonnull
  @Override
  public AppUser findUserByUserName(String userName) {
    return appUserRepository.findByUserName(userName);
  }

  @Nonnull
  @Override
  public List<AppUser> findUserByRol(AppUserRol rol) {
    return appUserRepository.findByRol(rol)
        .stream()
        .map(AppUser.class::cast)
        .collect(Collectors.toList());
  }
}
