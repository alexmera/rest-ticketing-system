package spring.ticketing.services;

import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import spring.ticketing.model.Resolution;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketDraft;
import spring.ticketing.model.TicketStatus;

public interface TicketsService {

  @Nonnull
  Ticket reportTicket(TicketDraft ticketDraft, Integer operatorId);

  @Nonnull
  Optional<Ticket> findTicketById(Integer id);

  @Nonnull
  Page<Ticket> findTickets(
      @Nullable Integer operatorId,
      @Nullable Integer clientId,
      @Nullable TicketStatus status,
      @Nullable Pageable pageable
  );

  @Nonnull
  Ticket updateTicket(
      @Nonnull Integer ticketId,
      @Nonnull Integer clientId,
      @Nonnull TicketChannel channel,
      @Nonnull String contactName,
      @Nonnull String subject,
      @Nullable String description
  );

  @Nonnull
  Ticket escalateTicket(
      @Nonnull Integer ticketId,
      @Nonnull Integer coordinatorId
  );

  @Nonnull
  Ticket closeTicket(
      @Nonnull Integer ticketId,
      @Nonnull Resolution resolution,
      @Nonnull String resolutionInfo
  );

  @Nonnull
  Ticket deleteTicket(@Nonnull Integer ticketId);
}
