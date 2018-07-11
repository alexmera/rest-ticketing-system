package spring.ticketing.model.data;

import javax.annotation.Nonnull;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;

@Data
public class AppUserData implements AppUser {

  @NotNull
  private Integer id;

  @NotEmpty
  @Nonnull
  private String userName;

  @NotEmpty
  @Email
  @Nonnull
  private String userEmail;

  @NotNull
  @Nonnull
  private AppUserRol rol;

  public AppUserData() {
  }

  public AppUserData(
      @Nonnull Integer id,
      @Nonnull String userName,
      @Nonnull String userEmail,
      @Nonnull AppUserRol rol
  ) {
    this.id = id;
    this.userName = userName;
    this.userEmail = userEmail;
    this.rol = rol;
  }

  public AppUserData(
      @Nonnull String userName,
      @Nonnull String userEmail,
      @Nonnull AppUserRol rol
  ) {
    this.userName = userName;
    this.userEmail = userEmail;
    this.rol = rol;
  }
}
