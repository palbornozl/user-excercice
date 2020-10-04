package cl.exercise.user.security;

import static java.util.Collections.emptyList;

import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserDetailsServiceImpl(
      UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @SneakyThrows
  @Override
  public UserDetails loadUserByUsername(String email) {
    UserEntity userEntity = userRepository.findByEmail(email);
    if (userEntity == null) {
      throw new UsernameNotFoundException(email);
    }
    return new User(
        userEntity.getEmail(), bCryptPasswordEncoder.encode(userEntity.getPassword()), emptyList());
  }
}
