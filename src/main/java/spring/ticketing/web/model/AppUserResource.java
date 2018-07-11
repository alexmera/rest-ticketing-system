package spring.ticketing.web.model;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AppUserResource implements AppUser {

  @NotNull
  private Integer id;

  @NotEmpty
  private String userName;

  @NotEmpty
  @Email
  private String userEmail;

  @NotNull
  private AppUserRol rol;

  private AppUserResource(AppUser other) {
    this.id = other.getId();
    this.userName = other.getUserName();
    this.userEmail = other.getUserEmail();
    this.rol = other.getRol();
  }

  public static AppUserResource from(AppUser other) {
    return new AppUserResource(other);
  }
}
