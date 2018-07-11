package spring.ticketing.web.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import spring.ticketing.web.model.HelloResource;

@RestController
public class Hello {

  @GetMapping("/hello")
  public HelloResource hello() {
    return new HelloResource("Peter", "Parker");
  }
}
