package spring.ticketing.web.controllers;

import java.net.URI;
import java.security.Principal;
import javax.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;
import spring.ticketing.model.Ticket;
import spring.ticketing.model.TicketStatus;
import spring.ticketing.services.AppUserService;
import spring.ticketing.services.ClientsService;
import spring.ticketing.services.TicketsService;
import spring.ticketing.web.model.InvalidOperationException;
import spring.ticketing.web.model.TicketCloseResource;
import spring.ticketing.web.model.TicketDraftResource;
import spring.ticketing.web.model.TicketResource;
import spring.ticketing.web.model.TicketUpdateResource;

@RestController
@RequestMapping("/api/tickets")
public class Tickets {

  private TicketsService ticketsService;

  private AppUserService appUserService;

  private ClientsService clientsService;

  public Tickets(TicketsService ticketsService,
      AppUserService appUserService, ClientsService clientsService) {
    this.ticketsService = ticketsService;
    this.appUserService = appUserService;
    this.clientsService = clientsService;
  }

  @ModelAttribute("authUser")
  public AppUser authAppUser(Principal principal) {
    if (principal != null) {
      return appUserService.findUserByUserName(principal.getName());
    }
    return null;
  }

  @GetMapping
  public Page<TicketResource> tickets(
      @ModelAttribute("authUser") AppUser authUser,
      @RequestParam(value = "page", defaultValue = "0") Integer page,
      @RequestParam(value = "size", defaultValue = "10", required = false) Integer size,
      Model model
  ) {
    Integer operatorId = authUser.getRol().equals(AppUserRol.COORDINATOR) ? null : authUser.getId();
    return ticketsService.findTickets(
        operatorId,
        null,
        null,
        PageRequest.of(page, size, Direction.DESC, "creationDate")
    ).map(TicketResource::from);
  }

  @GetMapping("/{id}")
  public TicketResource ticket(@PathVariable("id") Integer id) {
    return TicketResource.from(ticketsService.findTicketById(id).get());
  }

  @PostMapping
  public ResponseEntity<TicketResource> report(
      @ModelAttribute("authUser") AppUser authUser,
      @Valid @RequestBody TicketDraftResource ticketDraft
  ) {
    TicketResource ticket = TicketResource
        .from(ticketsService.reportTicket(ticketDraft, authUser.getId()));

    UriComponentsBuilder uriBuilder = MvcUriComponentsBuilder
        .fromMethodName(this.getClass(), "ticket", ticket.getId());
    URI locationUri = uriBuilder.build().toUri();

    return ResponseEntity.created(locationUri).body(ticket);
  }

  @PutMapping("/{id}")
  public TicketResource update(
      @ModelAttribute("authUser") AppUser authUser,
      @PathVariable("id") Integer id,
      @Valid @RequestBody TicketUpdateResource ticketUpdate
  ) {
    Ticket ticket = ticketsService.findTicketById(id).get();
    if (!ticket.getStatus().equals(TicketStatus.OPEN)) {
      throw new InvalidOperationException("The ticket to update must be open");
    }

    return TicketResource.from(
        ticketsService.updateTicket(
            id,
            ticketUpdate.getClient().getId(),
            ticketUpdate.getChannel(),
            ticketUpdate.getContactName(),
            ticketUpdate.getSubject(),
            ticketUpdate.getDescription()
        )
    );
  }

  @PatchMapping("/{id}/close")
  public TicketResource close(
      @PathVariable("id") Integer id,
      @Valid @RequestBody TicketCloseResource command
  ) {
    Ticket ticket = ticketsService.findTicketById(id).get();
    if (!ticket.getStatus().equals(TicketStatus.OPEN)) {
      throw new InvalidOperationException("The ticket to close must be open");
    }

    return TicketResource.from(
        ticketsService.closeTicket(
            id,
            command.getResolution(),
            command.getResolutionInfo()
        )
    );
  }

  @DeleteMapping("/{id}")
  public TicketResource delete(@PathVariable("id") Integer id) {
    return TicketResource.from(ticketsService.deleteTicket(id));
  }

}
