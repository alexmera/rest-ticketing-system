package spring.ticketing.web.model;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketDraft;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketDraftResource implements TicketDraft {

  @NotNull
  private ClientResource client;

  @NotNull
  private TicketChannel channel;

  @NotEmpty
  private String contactName;

  @NotEmpty
  private String subject;

  private TicketDraftResource(TicketDraft other) {
    this.client = ClientResource.from(other.getClient());
    this.channel = other.getChannel();
    this.contactName = other.getContactName();
    this.subject = other.getSubject();
  }

  public static TicketDraftResource from(TicketDraft other) {
    return new TicketDraftResource(other);
  }
}
