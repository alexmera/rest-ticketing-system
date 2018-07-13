package spring.ticketing.jwt.filters;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

public class JwtAuthenticationFailureHandler implements AuthenticationFailureHandler {

  private ObjectMapper objectMapper;

  public JwtAuthenticationFailureHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void onAuthenticationFailure(
      HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException exception
  ) throws IOException, ServletException {
    Map<String, Object> errorResponse = Map.of(
        "status", HttpStatus.UNAUTHORIZED,
        "statusCode", HttpStatus.UNAUTHORIZED.value(),
        "error", exception != null ? exception.getMessage() : ""
    );
    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_UTF8.toString());
    PrintWriter writer = response.getWriter();
    writer.write(objectMapper.writeValueAsString(errorResponse));
    writer.flush();
  }
}
