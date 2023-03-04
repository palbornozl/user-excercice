package cl.exercise.user.security;

import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.repository.UserRepository;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CustomAuthenticationProvider implements AuthenticationProvider {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationProvider(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        log.debug("--- authenticate ");
        String userName = authentication.getPrincipal().toString();
        String password = authentication.getCredentials().toString();

        UserEntity userEntity = userRepository.findByEmail(userName).orElseThrow(() -> new UsernameNotFoundException(userName));
        log.debug("Checking user password");

        if (passwordEncoder.matches(password, userEntity.getPassword())) {
            return new UsernamePasswordAuthenticationToken(
                    new UserDetailsImpl(userEntity), authentication.getCredentials());
        }

        log.error("contraseña erronea {}", userName);
        throw new UsernameNotFoundException("constraseña erronea");

    }

    @SneakyThrows
    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
