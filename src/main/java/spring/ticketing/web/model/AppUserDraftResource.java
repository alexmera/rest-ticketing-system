package spring.ticketing.web.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;

@Data
public class AppUserDraftResource implements AppUser {

  private Integer id;

  @NotEmpty
  private String userName;

  @NotEmpty
  @Email
  private String userEmail;

  @NotNull
  private AppUserRol rol;

  @NotEmpty
  private String password;
}
