package cl.exercise.user.security;

import cl.exercise.user.dto.UserRequestDTO;
import cl.exercise.user.dto.UserResponseDTO;
import cl.exercise.user.repository.UserRepository;
import cl.exercise.user.service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Collections;

import static cl.exercise.user.security.JWTConstants.HEADER_AUTHORIZATION;
import static cl.exercise.user.security.JWTConstants.TOKEN_PREFIX;

@Slf4j
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {

    private final TokenUtils tokenUtils;
    private final UserService service;
    private final UserRepository userRepository;
    private final AuthenticationManager authenticationManager;

    public JWTAuthenticationFilter(TokenUtils tokenUtils, UserService service, AuthenticationManager authenticationManager, UserRepository userRepository) {
        this.tokenUtils = tokenUtils;
        this.service = service;
        this.userRepository = userRepository;
        this.authenticationManager = authenticationManager;
        setFilterProcessesUrl("/authenticate");
        log.debug("calling login...");
    }

    @Override
    @SneakyThrows
    public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) {
        log.debug("--- attemptAuthentication");
        UserRequestDTO userRequestDTO = new ObjectMapper().readValue(req.getReader(), UserRequestDTO.class);

        UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                userRequestDTO.getUserEmail(),
                userRequestDTO.getUserPassword(),
                Collections.emptyList()
        );

        return authenticationManager.authenticate(usernamePasswordAuthenticationToken);
    }

    @Override
    @SneakyThrows
    protected void successfulAuthentication(
            HttpServletRequest req, HttpServletResponse res, FilterChain chain, Authentication auth) {
        log.debug("--- successfulAuthentication");
        UserDetailsImpl userDetails = (UserDetailsImpl) auth.getPrincipal();
        String token = tokenUtils.createToken(userDetails);

        res.addHeader(HEADER_AUTHORIZATION, TOKEN_PREFIX + token);
        res.setContentType("application/json");
        res.setCharacterEncoding("UTF-8");

        UserResponseDTO response = getUserResponseDTO(userDetails, token);
        res.getOutputStream().print(new ObjectMapper().writeValueAsString(response));
        res.flushBuffer();

        super.successfulAuthentication(req, res, chain, auth);
    }

    private UserResponseDTO getUserResponseDTO(UserDetailsImpl userDetails, String token) {
        userRepository.updateUserLastLogin(userDetails.getId(), token);
        UserRequestDTO request = UserRequestDTO.builder()
                .userEmail(userDetails.getUsername())
                .userPassword(userDetails.getPassword())
                .build();
        return service.getUserInformation(request);
    }
}
