package spring.ticketing.config;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.access.vote.RoleHierarchyVoter;
import org.springframework.security.access.vote.RoleVoter;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import spring.ticketing.jwt.filters.JwtAuthenticationFailureHandler;
import spring.ticketing.jwt.filters.JwtAuthenticationFilter;
import spring.ticketing.jwt.filters.JwtAuthorizationFilter;

@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  public static Algorithm JWT_SIGNING_ALGORITHM = Algorithm
      .HMAC256("TwLwCyXfFXuDRwbytFutFfEYhKG6yfVc2018$");

  public static JWTVerifier JWT_VERIFIER = JWT.require(JWT_SIGNING_ALGORITHM).build();

  public static String DEFAULT_ISSUER = "TicketingSys";
  public static String AUTHORIZATION_HEADER_NAME = "Authorization";
  public static String BEARER_TYPE = "Bearer";
  public static Integer EXPIRATION_HOURS = 8;

  @Autowired
  private ObjectMapper objectMapper;

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
        .cors()
        .and()
        .csrf().disable()
        .authorizeRequests()
        .antMatchers("/api/users/**", "/api/clients/**").hasAuthority("COORDINATOR")
        .antMatchers("/api/tickets/**").hasAuthority("OPERATOR")
        .anyRequest().authenticated()
        .and()
        .addFilterBefore(
            new JwtAuthenticationFilter(authenticationManager(), authenticationFailureHandler()),
            UsernamePasswordAuthenticationFilter.class)
        .addFilter(
            new JwtAuthorizationFilter(
                authenticationManager(),
                authenticationFailureHandler(),
                "/**"
            ));
  }

  @Bean
  public AuthenticationFailureHandler authenticationFailureHandler() {
    return new JwtAuthenticationFailureHandler(objectMapper);
  }

  @Bean
  public RoleHierarchy roleHierarchy() {
    RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
    hierarchy.setHierarchy("COORDINATOR > OPERATOR and OPERATOR > CLIENT");
    return hierarchy;
  }

  @Bean
  public RoleVoter roleVoter() {
    return new RoleHierarchyVoter(roleHierarchy());
  }
}
