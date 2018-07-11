package spring.ticketing.services;

import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;
import org.springframework.stereotype.Service;
import spring.ticketing.model.Client;

@Service
public interface ClientsService {

  @Nonnull
  List<Client> allClients();

  @Nonnull
  Optional<Client> findClientById(Integer id);

  @Nonnull
  Client createClient(Client client);

  @Nonnull
  Client updateClient(Client client);

  @Nonnull
  Client deleteClient(Integer id);

}
