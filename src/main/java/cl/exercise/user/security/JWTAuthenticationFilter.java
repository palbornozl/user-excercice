package cl.exercise.user.security;

import static cl.exercise.user.security.JWTConstants.EXPIRATION_TIME;
import static cl.exercise.user.security.JWTConstants.HEADER_STRING;
import static cl.exercise.user.security.JWTConstants.SECRET_PASSWORD;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;
import static com.auth0.jwt.algorithms.Algorithm.HMAC512;

import cl.exercise.user.dto.UserDTO;
import com.auth0.jwt.JWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.util.ArrayList;
import java.util.Date;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

  private final AuthenticationManager authenticationManager;

  public JWTAuthenticationFilter(AuthenticationManager authenticationManager) {
    this.authenticationManager = authenticationManager;
    setFilterProcessesUrl("/authenticate");
  }

  @Override
  @SneakyThrows
  public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
    UserDTO creds = new ObjectMapper().readValue(req.getInputStream(), UserDTO.class);

    return authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(
            creds.getUserEmail(), creds.getUserPassword(), new ArrayList<>()));
  }

  @Override
  @SneakyThrows
  protected void successfulAuthentication(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {

    String token =
        JWT.create()
            .withSubject(auth.getPrincipal().toString())
            .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
            .sign(HMAC512(SECRET_PASSWORD.getBytes()));

    ObjectMapper mapper = new ObjectMapper();
    ObjectNode result = mapper.createObjectNode();
    result.put("user", auth.getPrincipal().toString());
    result.put("token", token);

    res.addHeader(HEADER_STRING, TOKEN_PREFIX + token);
    res.getWriter().write(result.toPrettyString());
    res.getWriter().flush();
  }
}
