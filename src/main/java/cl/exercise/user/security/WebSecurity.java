package cl.exercise.user.security;

import static cl.exercise.user.security.JWTConstants.SIGN_UP_URL;

import cl.exercise.user.repository.UserRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

  @Value("${vault.my.secret}")
  private final String secretPassword;
  private final UserRepository userRepository;

  private final UserDetailsServiceImpl userDetailsService;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public WebSecurity(
      UserRepository userRepository,
      UserDetailsServiceImpl userDetailsService,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.userDetailsService = userDetailsService;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    secretPassword = null;
  }

  @SneakyThrows
  @Override
  protected void configure(HttpSecurity http) {
    http.cors()
        .and()
        .csrf()
        .disable()
        .authorizeRequests()
        .antMatchers(HttpMethod.POST, SIGN_UP_URL, "/authenticate")
        .anonymous()
        .antMatchers("/user/h2-console/**")
        .anonymous()
        .antMatchers("/user/in/**")
        .authenticated()
        .and()
        .addFilter(new JWTAuthenticationFilter(authenticationManager(), secretPassword))
        .addFilter(new JWTAuthorizationFilter(authenticationManager(), secretPassword))
        .sessionManagement()
        .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
    http.headers().frameOptions().disable();
  }

  @SneakyThrows
  @Override
  public void configure(AuthenticationManagerBuilder auth) {
    auth.authenticationProvider(new CustomAuthenticationProvider(userRepository));
    auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder);
  }

  @SneakyThrows
  @Bean
  CorsConfigurationSource corsConfigurationSource() {
    final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
    source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
    return source;
  }
}
