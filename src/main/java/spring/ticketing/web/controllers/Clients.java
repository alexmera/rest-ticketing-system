package spring.ticketing.web.controllers;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;
import javax.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;
import org.springframework.web.util.UriComponentsBuilder;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;
import spring.ticketing.services.AppUserService;
import spring.ticketing.services.ClientsService;
import spring.ticketing.web.model.AppUserResource;
import spring.ticketing.web.model.ClientResource;

@RestController
@RequestMapping("/api/clients")
public class Clients {

  private ClientsService clientsService;

  private AppUserService appUserService;

  public Clients(ClientsService clientsService, AppUserService appUserService) {
    this.clientsService = clientsService;
    this.appUserService = appUserService;
  }

  @GetMapping
  public List<ClientResource> clients() {
    return clientsService.allClients()
        .stream()
        .map(ClientResource::from)
        .collect(Collectors.toList());
  }

  @GetMapping("/{id}")
  public ClientResource client(@PathVariable("id") Integer id) {
    return ClientResource.from(clientsService.findClientById(id).get());
  }

  @PostMapping
  public ResponseEntity<ClientResource> post(
      @Valid @RequestBody ClientResource clientResource
  ) {
    AppUser clientUser = appUserService.findUserById(clientResource.getAppUser().getId()).get();
    if (!clientUser.getRol().equals(AppUserRol.CLIENT)) {
      throw new IllegalArgumentException("El appUser.rol debe ser CLIENT");
    }
    clientResource.setAppUser(AppUserResource.from(clientUser));

    ClientResource client = ClientResource.from(clientsService.createClient(clientResource));

    UriComponentsBuilder uriBuilder = MvcUriComponentsBuilder
        .fromMethodName(this.getClass(), "client", client.getId());
    URI locationUri = uriBuilder.build().toUri();

    return ResponseEntity.created(locationUri).body(client);
  }

  @PutMapping("/{id}")
  public ClientResource put(
      @PathVariable("id") Integer id,
      @Valid @RequestBody ClientResource clientResource
  ) {
    AppUser clientUser = appUserService.findUserById(clientResource.getAppUser().getId()).get();
    if (!clientUser.getRol().equals(AppUserRol.CLIENT)) {
      throw new IllegalArgumentException("El appUser.rol debe ser CLIENT");
    }
    clientResource.setId(id);
    clientResource.setAppUser(AppUserResource.from(clientUser));
    return ClientResource.from(clientsService.updateClient(clientResource));
  }

  @DeleteMapping("/{id}")
  public ClientResource delete(@PathVariable("id") Integer id) {
    return ClientResource.from(clientsService.deleteClient(id));
  }

}
