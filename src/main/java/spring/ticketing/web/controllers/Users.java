package spring.ticketing.web.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import spring.ticketing.repositories.AppUserDao;
import spring.ticketing.services.AppUserService;
import spring.ticketing.web.model.AppUserDraftResource;
import spring.ticketing.web.model.AppUserResource;
import spring.ticketing.web.model.AppUserUpdateResource;
import spring.ticketing.web.model.UpdatePasswordResource;

@RestController
@RequestMapping("/api/users")
public class Users {

  private AppUserService appUserService;

  private AppUserDao appUserDao;

  public Users(AppUserService appUserService, AppUserDao appUserDao) {
    this.appUserService = appUserService;
    this.appUserDao = appUserDao;
  }

  @GetMapping
  public List<AppUserResource> users() {
    return appUserService.allUsers()
        .stream()
        .map(AppUserResource::from)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public AppUserResource user(@PathVariable("id") Integer id) {
    return AppUserResource.from(appUserService.findUserById(id).get());
  }

  @PostMapping
  public ResponseEntity<AppUserResource> post(
      @Valid @RequestBody AppUserDraftResource appUserDraft
  ) {
    AppUserResource userResource = AppUserResource
        .from(appUserDao.create(appUserDraft, appUserDraft.getPassword()));

    UriComponentsBuilder uriBuilder = MvcUriComponentsBuilder
        .fromMethodName(this.getClass(), "user", userResource.getId());
    URI locationUri = uriBuilder.build().toUri();

    return ResponseEntity.created(locationUri).body(userResource);
  }

  @PutMapping("/{id}")
  public AppUserResource put(
      @PathVariable("id") Integer id,
      @Valid @RequestBody AppUserUpdateResource appUserUpdate
  ) {
    appUserUpdate.setId(id);
    return AppUserResource.from(appUserService.updateUser(appUserUpdate));
  }

  @PatchMapping("/{id}/password")
  public AppUserResource updatePassword(
      @PathVariable("id") Integer id,
      @Valid @RequestBody UpdatePasswordResource updatePassword
  ) {
    return AppUserResource.from(appUserDao.updatePassword(id, updatePassword.getPassword()));
  }

  @DeleteMapping("/{id}")
  public AppUserResource updatePassword(@PathVariable("id") Integer id) {
    return AppUserResource.from(appUserService.deleteUser(id));
  }

}
