package spring.ticketing.model.data;

import javax.annotation.Nonnull;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.Data;
import spring.ticketing.model.Client;
import spring.ticketing.model.jpa.AppUserJpa;

@Data
public class ClientData implements Client {

  private Integer id;

  @NotNull
  @Nonnull
  private AppUserJpa appUser;

  @NotEmpty
  @Nonnull
  private String clientName;

  public ClientData() {
  }

  public ClientData(@Nonnull Integer id, @Nonnull AppUserJpa appUser, @Nonnull String clientName) {
    this.id = id;
    this.appUser = appUser;
    this.clientName = clientName;
  }

  public ClientData(@Nonnull AppUserJpa appUser, @Nonnull String clientName) {
    this.appUser = appUser;
    this.clientName = clientName;
  }

  private ClientData(Client other) {
    this.id = other.getId();
    this.appUser = AppUserJpa.from(other.getAppUser());
    this.clientName = other.getClientName();
  }

  public static ClientData from(Client other) {
    return new ClientData(other);
  }
}
