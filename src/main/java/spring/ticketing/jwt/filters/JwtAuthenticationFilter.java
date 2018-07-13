package spring.ticketing.jwt.filters;

import static spring.ticketing.config.WebSecurityConfig.AUTHORIZATION_HEADER_NAME;
import static spring.ticketing.config.WebSecurityConfig.BEARER_TYPE;
import static spring.ticketing.config.WebSecurityConfig.DEFAULT_ISSUER;
import static spring.ticketing.config.WebSecurityConfig.JWT_SIGNING_ALGORITHM;
import static spring.ticketing.jwt.JwtUtils.authoritiesToArray;

import com.auth0.jwt.JWT;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.Objects;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.ticketing.config.WebSecurityConfig;
import spring.ticketing.jwt.JwtClaims;

public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private AuthenticationManager authenticationManager;

  private AuthenticationFailureHandler authenticationFailureHandler;

  /**
   * JWT based AuthenticationFilter.
   */
  public JwtAuthenticationFilter(
      AuthenticationManager authenticationManager,
      AuthenticationFailureHandler authenticationFailureHandler
  ) {
    super();
    this.authenticationManager = authenticationManager;
    this.authenticationFailureHandler = authenticationFailureHandler;
  }

  @Override
  public Authentication attemptAuthentication(
      HttpServletRequest request,
      HttpServletResponse response
  ) throws AuthenticationException {
    String username = Objects.requireNonNull(obtainUsername(request), "username is required");
    String password = Objects.requireNonNull(obtainPassword(request), "password is required");
    username = username.trim();
    UsernamePasswordAuthenticationToken auth =
        new UsernamePasswordAuthenticationToken(username, password);

    // Allow subclasses to set the "details" property
    setDetails(request, auth);
    return getAuthenticationManager().authenticate(auth);
  }

  @Override
  protected void successfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain,
      Authentication authResult
  ) {
    //Add eight hours to the current date
    LocalDateTime localDateTime = LocalDateTime.now().plusHours(WebSecurityConfig.EXPIRATION_HOURS);
    Date expirationTime = Date.from(localDateTime.atZone(ZoneId.systemDefault()).toInstant());

    String token = JWT
        .create()
        .withIssuer(DEFAULT_ISSUER)
        .withSubject(authResult.getName())
        .withExpiresAt(expirationTime)
        .withArrayClaim(
            JwtClaims.AUTHORITIES,
            authoritiesToArray(authResult.getAuthorities())
        )
        .sign(JWT_SIGNING_ALGORITHM);
    response.addHeader(AUTHORIZATION_HEADER_NAME, BEARER_TYPE + " " + token);
  }

  @Override
  protected void unsuccessfulAuthentication(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException failed
  ) throws IOException, ServletException {
    authenticationFailureHandler.onAuthenticationFailure(request, response, failed);
  }
  
  @Override
  protected AuthenticationManager getAuthenticationManager() {
    return authenticationManager;
  }
}
