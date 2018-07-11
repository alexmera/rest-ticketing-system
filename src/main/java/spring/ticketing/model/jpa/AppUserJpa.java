package spring.ticketing.model.jpa;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;

@Data
@Entity
@Table(name = "app_user")
@AllArgsConstructor
@NoArgsConstructor
public class AppUserJpa implements AppUser {

  @Id
  @SequenceGenerator(name = "app_user_generator", sequenceName = "app_user_id_seq", allocationSize = 1)
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_generator")
  private Integer id;

  @Column(name = "user_name", nullable = false)
  private String userName;

  @Column(name = "user_email", nullable = false)
  private String userEmail;

  @Enumerated(EnumType.STRING)
  @Column(name = "rol", nullable = false)
  private AppUserRol rol;

  public static AppUserJpa from(AppUser other) {
    return new AppUserJpa(
        other.getId(),
        other.getUserName(),
        other.getUserEmail(),
        other.getRol()
    );
  }
}
