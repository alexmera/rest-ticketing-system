package spring.ticketing.model.jpa;

import java.time.LocalDateTime;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.Resolution;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketStatus;

@Data
@Entity
@Table(name = "ticket")
@AllArgsConstructor
@NoArgsConstructor
public class TicketJpa implements Ticket {

  @Id
  @SequenceGenerator(name = "ticket_generator", sequenceName = "ticket_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ticket_generator")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "client_id", nullable = false)
  private ClientJpa client;

  @ManyToOne
  @JoinColumn(name = "operator_id", nullable = false)
  private AppUserJpa operator;

  @Enumerated(EnumType.STRING)
  @Column(name = "channel", nullable = false)
  private TicketChannel channel;

  @Column(name = "contact_name", nullable = false)
  private String contactName;

  @Column(name = "subject", nullable = false)
  private String subject;

  @Enumerated(EnumType.STRING)
  @Column(name = "status", nullable = false)
  private TicketStatus status;

  @Column(name = "escalated", nullable = false)
  private boolean escalated;

  @Column(name = "creation_date", nullable = false)
  private LocalDateTime creationDate;

  @Column(name = "modification_date", nullable = false)
  private LocalDateTime modificationDate;

  @ManyToOne
  @JoinColumn(name = "coordinator_id")
  private AppUserJpa coordinator;

  @Column(name = "description")
  private String description;

  @Enumerated(EnumType.STRING)
  @Column(name = "resolution")
  private Resolution resolution;

  @Column(name = "resolution_info")
  private String resolutionInfo;

  @Column(name = "closing_date")
  private LocalDateTime closingDate;

  private TicketJpa(Ticket other) {
    this.id = other.getId();
    this.client = ClientJpa.from(other.getClient());
    this.operator = AppUserJpa.from(other.getOperator());
    this.channel = other.getChannel();
    this.contactName = other.getContactName();
    this.subject = other.getSubject();
    this.status = other.getStatus();
    this.escalated = other.isEscalated();
    this.creationDate = other.getCreationDate();
    this.modificationDate = other.getModificationDate();
    this.coordinator =
        other.getCoordinator() != null ? AppUserJpa.from(other.getCoordinator()) : null;
    this.description = other.getDescription();
    this.resolution = other.getResolution();
    this.resolutionInfo = other.getResolutionInfo();
    this.closingDate = other.getClosingDate();
  }

  public static TicketJpa from(Ticket other) {
    return new TicketJpa(other);
  }

}
