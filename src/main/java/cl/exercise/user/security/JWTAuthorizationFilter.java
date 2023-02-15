package cl.exercise.user.security;

import static cl.exercise.user.security.JWTConstants.HEADER_STRING;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.ArrayList;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.SneakyThrows;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

public class JWTAuthorizationFilter extends BasicAuthenticationFilter {
  private final String secretPassword;

  public JWTAuthorizationFilter(AuthenticationManager authManager, String secretPassword) {
    super(authManager);
    this.secretPassword = secretPassword;
  }

  @Override
  @SneakyThrows
  protected void doFilterInternal(
      HttpServletRequest req, HttpServletResponse res, FilterChain chain) {
    String header = req.getHeader(HEADER_STRING);

    if (header == null || !header.startsWith(TOKEN_PREFIX)) {
      chain.doFilter(req, res);
      return;
    }

    UsernamePasswordAuthenticationToken authentication = getAuthentication(req);

    SecurityContextHolder.getContext().setAuthentication(authentication);
    chain.doFilter(req, res);
  }

  @SneakyThrows
  private UsernamePasswordAuthenticationToken getAuthentication(HttpServletRequest request) {
    String token = request.getHeader(HEADER_STRING);
    if (token != null) {
      String user =
          JWT.require(Algorithm.HMAC512(this.secretPassword.getBytes()))
              .build()
              .verify(token.replace(TOKEN_PREFIX, ""))
              .getSubject();

      if (user != null) {
        return new UsernamePasswordAuthenticationToken(user, null, new ArrayList<>());
      }
      return null;
    }
    return null;
  }
}
