package spring.ticketing.services;

import java.time.LocalDateTime;
import java.util.Optional;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import spring.ticketing.model.Resolution;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketChannel;
import spring.ticketing.model.TicketDraft;
import spring.ticketing.model.TicketStatus;
import spring.ticketing.model.jpa.AppUserJpa;
import spring.ticketing.model.jpa.ClientJpa;
import spring.ticketing.model.jpa.TicketJpa;
import spring.ticketing.repositories.jpa.AppUserRepository;
import spring.ticketing.repositories.jpa.ClientRepository;
import spring.ticketing.repositories.jpa.TicketRepository;

@Service
public class TicketsSpringService implements TicketsService {

  private TicketRepository ticketRepository;

  private AppUserRepository appUserRepository;

  private ClientRepository clientRepository;

  public TicketsSpringService(
      TicketRepository ticketRepository,
      AppUserRepository appUserRepository,
      ClientRepository clientRepository
  ) {
    this.ticketRepository = ticketRepository;
    this.appUserRepository = appUserRepository;
    this.clientRepository = clientRepository;
  }

  @Nonnull
  @Transactional
  @Override
  public Ticket reportTicket(TicketDraft ticketDraft, Integer operatorId) {
    LocalDateTime now = LocalDateTime.now();
    ClientJpa client = clientRepository.getOne(ticketDraft.getClient().getId());
    AppUserJpa operator = appUserRepository.getOne(operatorId);

    TicketJpa ticket = new TicketJpa();
    ticket.setClient(client);
    ticket.setChannel(ticketDraft.getChannel());
    ticket.setContactName(ticketDraft.getContactName());
    ticket.setSubject(ticketDraft.getSubject());
    ticket.setStatus(TicketStatus.OPEN);
    ticket.setEscalated(false);
    ticket.setOperator(operator);
    ticket.setCreationDate(now);
    ticket.setModificationDate(now);
    return ticketRepository.saveAndFlush(ticket);
  }

  @Nonnull
  @Override
  public Optional<Ticket> findTicketById(Integer id) {
    return ticketRepository.findById(id).map(Ticket.class::cast);
  }

  @Nonnull
  @Transactional
  @Override
  public Page<Ticket> findTickets(
      @Nullable Integer operatorId,
      @Nullable Integer clientId,
      @Nullable TicketStatus status,
      @Nullable Pageable pageable
  ) {
    ClientJpa client = clientId != null ? clientRepository.getOne(clientId) : null;
    AppUserJpa operator = (operatorId != null) ? appUserRepository.getOne(operatorId) : null;

    TicketJpa example = new TicketJpa();
    example.setOperator(operator);
    example.setClient(client);
    example.setStatus(status);
    return ticketRepository.findAll(
        Example.of(example),
        pageable != null ? pageable : Pageable.unpaged()
    ).map(Ticket.class::cast);
  }

  @Nonnull
  @Transactional
  @Override
  public Ticket updateTicket(
      @Nonnull Integer ticketId,
      @Nonnull Integer clientId,
      @Nonnull TicketChannel channel,
      @Nonnull String contactName,
      @Nonnull String subject,
      @Nullable String description
  ) {
    TicketJpa ticket = ticketRepository.getOne(ticketId);
    ClientJpa client = clientRepository.getOne(clientId);
    ticket.setClient(client);
    ticket.setChannel(channel);
    ticket.setContactName(contactName);
    ticket.setSubject(subject);
    ticket.setDescription(description);
    ticket.setModificationDate(LocalDateTime.now());
    return ticketRepository.saveAndFlush(ticket);
  }

  @Nonnull
  @Transactional
  @Override
  public Ticket escalateTicket(@Nonnull Integer ticketId, @Nonnull Integer coordinatorId) {
    AppUserJpa coordinator = appUserRepository.getOne(coordinatorId);

    TicketJpa ticket = ticketRepository.getOne(ticketId);
    ticket.setCoordinator(coordinator);
    ticket.setEscalated(true);
    ticket.setModificationDate(LocalDateTime.now());
    return ticketRepository.saveAndFlush(ticket);
  }

  @Nonnull
  @Transactional
  @Override
  public Ticket closeTicket(
      @Nonnull Integer ticketId,
      @Nonnull Resolution resolution,
      @Nonnull String resolutionInfo
  ) {
    TicketJpa ticket = ticketRepository.getOne(ticketId);
    ticket.setResolution(resolution);
    ticket.setResolutionInfo(resolutionInfo);
    ticket.setStatus(TicketStatus.CLOSED);
    ticket.setClosingDate(LocalDateTime.now());
    return ticketRepository.saveAndFlush(ticket);
  }

  @Nonnull
  @PreAuthorize("hasAuthority('COORDINATOR')")
  @Transactional
  @Override
  public Ticket deleteTicket(@Nonnull Integer ticketId) {
    TicketJpa ticket = ticketRepository.getOne(ticketId);
    ticketRepository.deleteById(ticketId);
    return ticket;
  }
}
