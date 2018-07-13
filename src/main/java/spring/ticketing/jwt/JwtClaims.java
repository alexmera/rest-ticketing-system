package spring.ticketing.jwt;

public class JwtClaims {

  public static final String AUTHORITIES = "authorities";

  private JwtClaims() {
    throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
  }
}
