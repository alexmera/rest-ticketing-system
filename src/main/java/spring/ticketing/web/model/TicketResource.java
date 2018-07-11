package spring.ticketing.web.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.Resolution;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketStatus;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TicketResource implements Ticket {

  private Integer id;

  private ClientResource client;

  private AppUserResource operator;

  private TicketChannel channel;

  private String contactName;

  private String subject;

  private TicketStatus status;

  private boolean escalated;

  private LocalDateTime creationDate;

  private LocalDateTime modificationDate;

  private AppUserResource coordinator;

  private String description;

  private Resolution resolution;

  private String resolutionInfo;

  private LocalDateTime closingDate;

  private TicketResource(Ticket other) {
    this.id = other.getId();
    this.client = ClientResource.from(other.getClient());
    this.operator = AppUserResource.from(other.getOperator());
    this.channel = other.getChannel();
    this.contactName = other.getContactName();
    this.subject = other.getSubject();
    this.status = other.getStatus();
    this.escalated = other.isEscalated();
    this.creationDate = other.getCreationDate();
    this.modificationDate = other.getModificationDate();
    this.coordinator =
        other.getCoordinator() != null ? AppUserResource.from(other.getCoordinator()) : null;
    this.description = other.getDescription();
    this.resolution = other.getResolution();
    this.resolutionInfo = other.getResolutionInfo();
    this.closingDate = other.getClosingDate();
  }

  public static TicketResource from(Ticket other) {
    return new TicketResource(other);
  }

}
