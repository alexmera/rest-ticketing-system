package spring.ticketing.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.Resolution;
import spring.ticketing.model.Ticket;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketCloseResource {

  @NotNull
  private Resolution resolution;

  @NotEmpty
  private String resolutionInfo;

  private TicketCloseResource(Ticket other) {
    this.resolution = other.getResolution();
    this.resolutionInfo = other.getResolutionInfo();
  }

  public static TicketCloseResource from(Ticket other) {
    return new TicketCloseResource(other);
  }
}
