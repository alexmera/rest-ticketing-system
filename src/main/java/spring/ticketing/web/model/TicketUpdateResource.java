package spring.ticketing.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketDraft;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketUpdateResource implements TicketDraft {

  private Integer id;

  @NotNull
  private ClientResource client;

  @NotNull
  private TicketChannel channel;

  @NotEmpty
  private String contactName;

  @NotEmpty
  private String subject;

  private String description;

  private TicketUpdateResource(Ticket other) {
    this.id = other.getId();
    this.client = ClientResource.from(other.getClient());
    this.channel = other.getChannel();
    this.contactName = other.getContactName();
    this.subject = other.getSubject();
    this.description = other.getDescription();
  }

  public static TicketUpdateResource from(Ticket other) {
    return new TicketUpdateResource(other);
  }
}
