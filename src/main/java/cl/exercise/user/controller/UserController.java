package cl.exercise.user.controller;

import cl.exercise.user.dto.UserLoginDTO;
import cl.exercise.user.dto.UserRequestDTO;
import cl.exercise.user.dto.UserResponseDTO;
import cl.exercise.user.security.TokenUtils;
import cl.exercise.user.service.UserService;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

import static cl.exercise.user.security.JWTConstants.HEADER_AUTHORIZATION;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;
import static cl.exercise.user.transformer.Transformer.userLoginTouserRequest;

@RestController
@Slf4j
@RequestMapping
@Transactional(transactionManager = "transactionManagerUser")
public class UserController {

    public static final String RESPONSE_RESULTS_STG = "Response results: {}";
    public static final String ERROR_STG = "[Error] ";

    private final TokenUtils tokenUtils;
    private final UserService service;

    public UserController(TokenUtils tokenUtils, UserService service) {
        this.tokenUtils = tokenUtils;
        this.service = service;
    }

    @SneakyThrows
    @PostMapping("/sign-up")
    public ResponseEntity<UserResponseDTO> signUp(
            @Valid @RequestBody UserRequestDTO request, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Validation problems [sign-up]... {}", result.getAllErrors());
            throw new IllegalArgumentException(
                    ERROR_STG + result.getAllErrors().get(0).getDefaultMessage());
        }

        if (service.isEmailExists(request.getUserEmail())) {
            throw new IllegalArgumentException("[Error] El correo ya está registrado");
        }

        UserResponseDTO response = service.saveUser(request);
        log.debug(RESPONSE_RESULTS_STG, response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SneakyThrows
    @PostMapping("/authenticate")
    public ResponseEntity<UserResponseDTO> login(
            @Valid @RequestBody UserLoginDTO request, BindingResult result) {
        if (result.hasErrors()) {
            log.error("Validation problems [sign-up]... {}", result.getAllErrors());
            throw new IllegalArgumentException(
                    ERROR_STG + result.getAllErrors().get(0).getDefaultMessage());
        }

        UserResponseDTO response = service.getUserInformation(userLoginTouserRequest(request));
        log.debug(RESPONSE_RESULTS_STG, response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SneakyThrows
    @GetMapping("/in/get")
    public ResponseEntity<UserResponseDTO> getInfo(@RequestHeader(value = HEADER_AUTHORIZATION) String token) {
        UserResponseDTO response = service.getUserCompleteInformation(tokenUtils.decodeToken(token));
        log.debug(RESPONSE_RESULTS_STG, response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }

    @SneakyThrows
    @PutMapping("/in/update")
    public ResponseEntity<UserResponseDTO> update(
            @Valid @RequestBody UserRequestDTO request,
            BindingResult result,
            @RequestHeader(value = HEADER_AUTHORIZATION) String token) {
        if (result.hasErrors()) {
            log.error("Validation problems [update]... {}", result.getAllErrors());
            throw new IllegalArgumentException(
                    ERROR_STG + result.getAllErrors().get(0).getDefaultMessage());
        }

        UserResponseDTO response = service.updateUser(request, token.replace(TOKEN_PREFIX, "").trim());
        log.debug(RESPONSE_RESULTS_STG, response.toString());

        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
