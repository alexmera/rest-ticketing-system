package spring.ticketing.web.controllers;

import java.util.stream.Collectors;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.ticketing.web.model.HelloResource;

@RestController
public class Hello {

  @GetMapping("/hello")
  public HelloResource hello(Authentication authentication) {
    return new HelloResource(
        authentication.getName(),
        authentication.getAuthorities()
            .stream()
            .map(GrantedAuthority::getAuthority)
            .collect(Collectors.toList())
    );
  }
}
