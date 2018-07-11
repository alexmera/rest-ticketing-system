package spring.ticketing.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
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
import spring.ticketing.model.Client;

@Data
@Entity
@Table(name = "client")
@AllArgsConstructor
@NoArgsConstructor
public class ClientJpa implements Client {

  @Id
  @SequenceGenerator(name = "client_generator", sequenceName = "client_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "client_generator")
  private Integer id;

  @ManyToOne
  @JoinColumn(name = "app_user_id", nullable = false)
  private AppUserJpa appUser;

  @Column(name = "client_name", nullable = false)
  private String clientName;

  public static ClientJpa from(Client other) {
    return new ClientJpa(
        other.getId(),
        AppUserJpa.from(other.getAppUser()),
        other.getClientName()
    );
  }
}
