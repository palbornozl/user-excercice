package cl.exercise.user.config;

import cl.exercise.user.exception.GlobalExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ErrorExceptionConfig {
  @Bean
  public GlobalExceptionHandler getExceptionHandler() {
    return new GlobalExceptionHandler();
  }
}
