package spring.ticketing.jwt.filters;

import static spring.ticketing.config.WebSecurityConfig.AUTHORIZATION_HEADER_NAME;
import static spring.ticketing.config.WebSecurityConfig.BEARER_TYPE;
import static spring.ticketing.config.WebSecurityConfig.JWT_VERIFIER;
import static spring.ticketing.jwt.JwtUtils.arrayToAuthorities;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.google.common.base.Preconditions;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;
import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.www.BasicAuthenticationEntryPoint;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import spring.ticketing.jwt.JwtClaims;

public class JwtAuthorizationFilter extends BasicAuthenticationFilter {

  private RequestMatcher requestMatcher;

  private AuthenticationFailureHandler authenticationFailureHandler;

  /**
   * JWT based AuthorizationFilter.
   */
  public JwtAuthorizationFilter(
      @Nonnull AuthenticationManager authenticationManager,
      @Nonnull AuthenticationFailureHandler authenticationFailureHandler,
      @Nonnull RequestMatcher requestMatcher
  ) {
    super(authenticationManager, new BasicAuthenticationEntryPoint());
    this.requestMatcher = requestMatcher;
    this.authenticationFailureHandler = authenticationFailureHandler;
  }

  /**
   * JWT based AuthorizationFilter.
   */
  public JwtAuthorizationFilter(
      @Nonnull AuthenticationManager authenticationManager,
      @Nonnull AuthenticationFailureHandler authenticationFailureHandler,
      @Nonnull String... antPatterns
  ) {
    super(authenticationManager, new BasicAuthenticationEntryPoint());
    this.authenticationFailureHandler = authenticationFailureHandler;
    List<RequestMatcher> requestMatcherList = List.of(antPatterns)
        .stream()
        .map(AntPathRequestMatcher::new)
        .collect(Collectors.toList());
    this.requestMatcher = new OrRequestMatcher(requestMatcherList);
  }

  private static String extractBearerValue(@Nonnull HttpServletRequest request) {
    String authHeader = request.getHeader(AUTHORIZATION_HEADER_NAME);
    Preconditions.checkState(
        authHeader != null && !authHeader.trim().isEmpty(),
        "%s header is not present in this request",
        AUTHORIZATION_HEADER_NAME
    );
    Preconditions.checkState(
        StringUtils.startsWithIgnoreCase(authHeader, BEARER_TYPE),
        "%s header content doesn't starts with %s",
        AUTHORIZATION_HEADER_NAME,
        BEARER_TYPE
    );
    return StringUtils.removeStartIgnoreCase(authHeader, BEARER_TYPE).trim();
  }

  private static Authentication jwtTokenToAuthentication(@Nonnull DecodedJWT decodedJwt) {
    return new UsernamePasswordAuthenticationToken(
        decodedJwt.getSubject(),
        decodedJwt.getToken(),
        arrayToAuthorities(
            decodedJwt.getClaim(JwtClaims.AUTHORITIES).asArray(String.class)
        )
    );
  }

  @Override
  protected void doFilterInternal(
      HttpServletRequest request,
      HttpServletResponse response,
      FilterChain chain
  ) throws IOException, ServletException {
    if (requiresValidToken(request)) {
      try {
        Authentication authentication = authenticate(request);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        chain.doFilter(request, response);
      } catch (AuthenticationException e) {
        SecurityContextHolder.clearContext();
        authenticationFailureHandler.onAuthenticationFailure(
            request,
            response,
            e
        );
      } catch (Exception e) {
        throw new IllegalStateException(e);
      }
    } else {
      chain.doFilter(request, response);
    }
  }

  private Authentication authenticate(HttpServletRequest request) {
    String bearerValue = null;
    try {
      bearerValue = extractBearerValue(request);
      DecodedJWT jwt = JWT_VERIFIER.verify(bearerValue);
      return jwtTokenToAuthentication(jwt);
    } catch (Exception e) {
      if (bearerValue != null) {
        throw new BadCredentialsException("Invalid JWT token", e);
      }
      throw new AuthenticationCredentialsNotFoundException(e.getMessage());
    }
  }

  private boolean requiresValidToken(HttpServletRequest request) {
    return requestMatcher.matches(request);
  }
}
