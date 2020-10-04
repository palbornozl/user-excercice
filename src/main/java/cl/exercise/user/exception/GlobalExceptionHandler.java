package cl.exercise.user.exception;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@Slf4j
@ControllerAdvice
@RestController
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {

  @ExceptionHandler(cl.exercise.user.exception.ResourceException.class)
  public ResponseEntity<?> handleResponseExceptions(
      cl.exercise.user.exception.ResourceException ex, WebRequest request)
      throws JsonProcessingException {
    return getResponseEntity(ex, request);
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<?> handleAllExceptions(Exception ex, WebRequest request)
      throws JsonProcessingException {

    return getResponseEntity(ex, request);
  }

  private ResponseEntity<?> getResponseEntity(Exception ex, WebRequest request)
      throws JsonProcessingException {
    ErrorDetails error =
        ErrorDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.INTERNAL_SERVER_ERROR.value())
            .detail(ex.getMessage())
            .title(HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase())
            .source(request.getDescription(false))
            .build();
    cl.exercise.user.exception.ErrorList errors =
        cl.exercise.user.exception.ErrorList.builder().errors(Arrays.asList(error)).build();

    log.error((new ObjectMapper()).writeValueAsString(errors));
    return new ResponseEntity<>(errors, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  @Override
  protected ResponseEntity<Object> handleHttpMessageNotReadable(
      HttpMessageNotReadableException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    ErrorDetails error =
        ErrorDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .detail(ex.getMessage())
            .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .source(request.getDescription(false))
            .build();
    cl.exercise.user.exception.ErrorList errors =
        cl.exercise.user.exception.ErrorList.builder().errors(Arrays.asList(error)).build();
    try {
      log.error((new ObjectMapper()).writeValueAsString(errors));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }
    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  protected ResponseEntity<Object> handleMethodArgumentNotValid(
      MethodArgumentNotValidException ex,
      HttpHeaders headers,
      HttpStatus status,
      WebRequest request) {

    List<String> errorsValidation =
        ex.getBindingResult().getFieldErrors().stream()
            .map(x -> x.getDefaultMessage())
            .collect(Collectors.toList());

    ErrorDetails error =
        ErrorDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .detail(errorsValidation.toString())
            .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .source(request.getDescription(false))
            .build();
    cl.exercise.user.exception.ErrorList errors =
        cl.exercise.user.exception.ErrorList.builder().errors(Arrays.asList(error)).build();
    try {
      log.error((new ObjectMapper()).writeValueAsString(errors));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }

  @Override
  protected ResponseEntity<Object> handleBindException(
      BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
    List<String> errorsValidation =
        ex.getBindingResult().getFieldErrors().stream()
            .map(x -> x.getDefaultMessage())
            .collect(Collectors.toList());

    ErrorDetails error =
        ErrorDetails.builder()
            .timestamp(LocalDateTime.now())
            .status(HttpStatus.BAD_REQUEST.value())
            .detail(errorsValidation.toString())
            .title(HttpStatus.BAD_REQUEST.getReasonPhrase())
            .source(request.getDescription(false))
            .build();
    cl.exercise.user.exception.ErrorList errors =
        cl.exercise.user.exception.ErrorList.builder().errors(Arrays.asList(error)).build();
    try {
      log.error((new ObjectMapper()).writeValueAsString(errors));
    } catch (JsonProcessingException e) {
      e.printStackTrace();
    }

    return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
  }
}
