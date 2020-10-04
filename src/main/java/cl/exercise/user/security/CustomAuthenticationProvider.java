package cl.exercise.user.security;

import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.repository.UserRepository;
import java.sql.Timestamp;
import java.time.Instant;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

  private final UserRepository userRepository;

  public CustomAuthenticationProvider(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  @SneakyThrows
  @Override
  public Authentication authenticate(Authentication authentication) throws AuthenticationException {
    String userName = authentication.getPrincipal().toString();
    String password = authentication.getCredentials().toString();

    UserEntity userEntity = userRepository.findByEmail(userName);
    log.info("Checking user password");
    if (userEntity != null && password.equals(userEntity.getPassword())) {
      log.info("Update last login");
      userRepository.updateUserLastLogin(userEntity.getId(), Timestamp.from(Instant.now()));
      return new UsernamePasswordAuthenticationToken(
          authentication.getPrincipal(), authentication.getCredentials());
    }

    log.error("Password incorrect");
    throw new UsernameNotFoundException(userName);
  }

  @SneakyThrows
  @Override
  public boolean supports(Class<?> authentication) {
    return authentication.equals(UsernamePasswordAuthenticationToken.class);
  }
}
