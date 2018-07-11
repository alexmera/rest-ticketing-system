package spring.ticketing.web.model;

import javax.validation.constraints.NotEmpty;
import lombok.Data;

@Data
public class UpdatePasswordResource {

  @NotEmpty
  private String password;
}
