package cl.exercise.user.security;

import cl.exercise.user.repository.UserRepository;
import cl.exercise.user.service.UserService;
import lombok.SneakyThrows;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import static cl.exercise.user.security.JWTConstants.SIGN_UP_URL;

@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    private final TokenUtils tokenUtils;
    private final UserService service;

    private final UserRepository userRepository;

    private final UserDetailsServiceImpl userDetailsService;

    private final PasswordEncoder passwordEncoder;

    public WebSecurity(
            TokenUtils tokenUtils, UserService service, UserRepository userRepository,
            UserDetailsServiceImpl userDetailsService,
            PasswordEncoder passwordEncoder) {
        this.tokenUtils = tokenUtils;
        this.service = service;
        this.userRepository = userRepository;
        this.userDetailsService = userDetailsService;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    @Override
    protected void configure(HttpSecurity http) {
        http.cors()
                .and()
                .csrf()
                .disable()
                .authorizeRequests()
                .antMatchers(HttpMethod.POST, SIGN_UP_URL, "/authenticate").anonymous()
                .antMatchers("/user/h2-console/**").anonymous()
                .antMatchers("/user/swagger-ui/**").anonymous()
                .antMatchers("/user/in/**").authenticated()
                .and()
                .addFilter(new JWTAuthenticationFilter(tokenUtils, service, authenticationManager(), userRepository))
                .addFilter(new JWTAuthorizationFilter(authenticationManager(), tokenUtils))
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
        http.headers().frameOptions().disable();
    }

    @SneakyThrows
    @Override
    public void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(new CustomAuthenticationProvider(userRepository, passwordEncoder));
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
    }

    @SneakyThrows
    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
        return source;
    }
}
