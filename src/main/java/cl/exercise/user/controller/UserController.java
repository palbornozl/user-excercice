package cl.exercise.user.controller;

import static cl.exercise.user.security.JWTConstants.HEADER_STRING;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;

import cl.exercise.user.dto.UserDTO;
import cl.exercise.user.dto.UserResponse;
import cl.exercise.user.service.UserService;
import javax.validation.Valid;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
@RequestMapping
@Transactional(transactionManager = "transactionManagerUser")
public class UserController {

  private final UserService service;

  public UserController(UserService service) {
    this.service = service;
  }

  @SneakyThrows
  @PostMapping("/sign-up")
  public ResponseEntity<UserResponse> signUp(
      @Valid @RequestBody UserDTO request, BindingResult result) {
    if (result.hasErrors()) {
      log.error("Validation problems [sign-up]... {}", result.getAllErrors().toString());
      throw new IllegalArgumentException(
          "[Error] " + result.getAllErrors().get(0).getDefaultMessage());
    }

    if (service.findUserByEmail(request.getUserEmail()) != null) {
      throw new IllegalArgumentException("[Error] El correo ya está registrado");
    }

    request.setUserToken(request.getUserPassword());
    UserResponse response = service.saveUser(request);
    log.debug("Response results: {}", response.toString());

    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @SneakyThrows
  @PostMapping("/in/sign-in")
  public ResponseEntity<UserDTO> login(
      @Valid @RequestBody UserDTO request,
      BindingResult result,
      @RequestHeader(value = HEADER_STRING) String token) {
    if (result.hasErrors()) {
      log.error("Validation problems [sign-in]... {}", result.getAllErrors().toString());
      throw new IllegalArgumentException(
          "[Error] " + result.getAllErrors().get(0).getDefaultMessage());
    }

    request.setUserToken(token.replace(TOKEN_PREFIX, "").trim());
    UserDTO response = service.getUserInformation(request);
    log.debug("Response results: {}", response.toString());

    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }

  @SneakyThrows
  @PutMapping("/in/update")
  public ResponseEntity<UserResponse> update(
      @Valid @RequestBody UserDTO request,
      BindingResult result,
      @RequestHeader(value = HEADER_STRING) String token) {
    if (result.hasErrors()) {
      log.error("Validation problems [update]... {}", result.getAllErrors().toString());
      throw new IllegalArgumentException(
          "[Error] " + result.getAllErrors().get(0).getDefaultMessage());
    }

    request.setUserToken(token.replace(TOKEN_PREFIX, "").trim());
    UserResponse response = service.updateUser(request);
    log.debug("Response results: {}", response.toString());

    return new ResponseEntity<>(response, response == null ? HttpStatus.NO_CONTENT : HttpStatus.OK);
  }
}
