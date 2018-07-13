package spring.ticketing.jwt;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class JwtUtils {

  public static String[] authoritiesToArray(
      @Nonnull Collection<? extends GrantedAuthority> authorities
  ) {
    return authorities
        .stream()
        .map(GrantedAuthority::getAuthority)
        .collect(Collectors.toList())
        .toArray(new String[]{});
  }

  public static List<GrantedAuthority> arrayToAuthorities(@Nonnull String... authorities) {
    return List.of(authorities)
        .stream()
        .map(SimpleGrantedAuthority::new)
        .collect(Collectors.toList());
  }
}
