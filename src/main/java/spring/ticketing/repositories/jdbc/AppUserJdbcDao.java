package spring.ticketing.repositories.jdbc;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcOperations;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.simple.SimpleJdbcInsertOperations;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;
import spring.ticketing.model.AppUser;
import spring.ticketing.model.AppUserRol;
import spring.ticketing.model.data.AppUserData;
import spring.ticketing.repositories.AppUserDao;

@Repository
public class AppUserJdbcDao implements AppUserDao, UserDetailsService {

  private static final RowMapper<AppUser> rowMapper = (rs, rowNum) -> new AppUserData(
      rs.getInt("id"),
      rs.getString("user_name"),
      rs.getString("user_email"),
      AppUserRol.valueOf(rs.getString("rol"))
  );
  private final JdbcOperations jdbc;
  private final SimpleJdbcInsertOperations jdbcInsert;
  private final PasswordEncoder passwordEncoder;

  public AppUserJdbcDao(
      JdbcOperations jdbc,
      @Qualifier("appUserJdbcInsert") SimpleJdbcInsertOperations jdbcInsert,
      PasswordEncoder passwordEncoder
  ) {
    this.jdbc = jdbc;
    this.jdbcInsert = jdbcInsert;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public AppUser create(AppUser entity, String rawPassword) {
    Map<String, Object> args = new HashMap<>();
    args.put("user_name", entity.getUserName());
    args.put("user_email", entity.getUserEmail());
    args.put("rol", entity.getRol().name());
    args.put("password", passwordEncoder.encode(rawPassword));

    Number id = jdbcInsert.executeAndReturnKey(args);
    return getOne(id.intValue());
  }

  @Override
  public AppUser getOne(Integer id) {
    return jdbc.queryForObject("SELECT * FROM app_user WHERE id = ?", rowMapper, id);
  }

  @Override
  public Optional<AppUser> findById(Integer id) {
    try {
      return Optional.of(getOne(id));
    } catch (EmptyResultDataAccessException e) {
      return Optional.empty();
    }
  }

  @Override
  public List<AppUser> all() {
    return jdbc.query("SELECT * FROM app_user", rowMapper);
  }

  @Override
  public AppUser update(AppUser entity) {
    String query = "UPDATE app_user SET user_name = ?, user_email = ?, rol = ? WHERE id = ?";
    int num = jdbc.update(
        query,
        entity.getUserName(),
        entity.getUserEmail(),
        entity.getRol().name(),
        entity.getId()
    );
    if (num == 1) {
      return getOne(entity.getId());
    }
    throw new IllegalStateException(
        "The update operation must affect exactly one row. Rows affected " + num
    );
  }

  @Override
  public AppUser delete(Integer id) {
    AppUser appUser = getOne(id);
    String query = "DELETE FROM app_user WHERE id = ?";
    int num = jdbc.update(query, id);
    if (num == 1) {
      return appUser;
    }
    throw new IllegalStateException(
        "The delete operation must affect exactly one row. Rows affected " + num
    );
  }

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Map<String, Object> data =
        jdbc.queryForMap("SELECT * FROM app_user WHERE user_name = ?", username);
    return User.builder()
        .username(data.get("user_name").toString())
        .password(data.get("password").toString())
        .authorities(data.get("rol").toString())
        .build();
  }
}
