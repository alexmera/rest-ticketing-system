package spring.ticketing.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.Client;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientResource implements Client {

  private Integer id;

  @NotNull
  private AppUserResource appUser;

  @NotEmpty
  private String clientName;

  private ClientResource(Client other) {
    this.id = other.getId();
    this.appUser = AppUserResource.from(other.getAppUser());
    this.clientName = other.getClientName();
  }

  public static ClientResource from(Client other) {
    return new ClientResource(other);
  }
}
