package cl.exercise.user.service;

import cl.exercise.user.dto.UserPhoneDTO;
import cl.exercise.user.dto.UserRequestDTO;
import cl.exercise.user.dto.UserResponseDTO;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.entities.UserPhoneEntity;
import cl.exercise.user.repository.UserPhoneRepository;
import cl.exercise.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.transaction.Transactional;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static cl.exercise.user.transformer.Transformer.userCompleteInformation;
import static cl.exercise.user.transformer.Transformer.userEntityToResponse;

@Service
@Slf4j
public class UserService {

    private final UserRepository userRepository;

    private final UserPhoneRepository userPhoneRepository;

    private final PasswordEncoder passwordEncoder;

    public UserService(
            UserRepository userRepository,
            UserPhoneRepository userPhoneRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.userPhoneRepository = userPhoneRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @SneakyThrows
    public UserResponseDTO saveUser(UserRequestDTO request) {
        request.setUserPassword(passwordEncoder.encode(request.getUserPassword()));
        log.debug("--> saving user {}", request.toString());

        UUID userId =
                saveUserInfo(
                        UserEntity.builder()
                                .email(request.getUserEmail())
                                .name(request.getUserName())
                                .password(request.getUserPassword())
                                .token(request.getUserPassword())
                                .isActive(true)
                                .build());

        if (!CollectionUtils.isEmpty(request.getUserPhoneDTO())) {
            log.debug("--> saving phone {}", request.getUserPhoneDTO().toString());
            saveUserPhoneInfo(request.getUserPhoneDTO(), userId);
        }

        UserEntity userEntity = userRepository.findById(userId).orElseThrow(NullPointerException::new);
        log.debug("{}", new ObjectMapper().writeValueAsString(userEntity));

        return generateResponse(userEntity);
    }

    @SneakyThrows
    public boolean isEmailExists(String email) {
        return userRepository.findByEmail(email).isPresent();
    }

    @SneakyThrows
    public UserResponseDTO getUserCompleteInformation(String email) {
        log.debug("--> getting user info {}", email);
        UserEntity userEntity = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException(email));
        log.debug("{}", new ObjectMapper().writeValueAsString(userEntity));

        List<UserPhoneEntity> userPhoneEntity = userPhoneRepository.findByIdUser(userEntity.getId());
        log.debug("{}", new ObjectMapper().writeValueAsString(userPhoneEntity));

        UserResponseDTO responseDTO = userEntityToResponse(userEntity);

        return userCompleteInformation(responseDTO, userEntity, userPhoneEntity);
    }

    @SneakyThrows
    public UserResponseDTO getUserInformation(UserRequestDTO request) {
        log.info("--> getting user info {}", request.toString());
        UserEntity userEntity = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUserEmail()));
        return generateResponse(userEntity);
    }

    public UserResponseDTO updateUser(UserRequestDTO request, String token) {
        log.debug("--> update user {}", request.toString());
        UserEntity userEntity = userRepository.findByEmail(request.getUserEmail())
                .orElseThrow(() -> new UsernameNotFoundException(request.getUserEmail()));

        userRepository.updateUserInfo(
                userEntity.getId(),
                request.getUserName(),
                request.getUserEmail(),
                passwordEncoder.encode(request.getUserPassword()),
                token,
                Timestamp.from(Instant.now()));

        List<UserPhoneEntity> phoneEntities = userPhoneRepository.findByIdUser(userEntity.getId());
        if (!CollectionUtils.isEmpty(phoneEntities)) {
            log.debug("--> delete phone {}", phoneEntities);
            userPhoneRepository.deleteByIdUser(userEntity.getId());
        }
        if (!CollectionUtils.isEmpty(request.getUserPhoneDTO())) {
            log.debug("--> saving phone {}", request.getUserPhoneDTO().toString());
            saveUserPhoneInfo(request.getUserPhoneDTO(), userEntity.getId());
        }
        UserEntity userEntityOptional = userRepository.findById(userEntity.getId())
                .orElseThrow(NullPointerException::new);
        return generateResponse(userEntityOptional);
    }

    @Transactional
    UUID saveUserInfo(UserEntity userEntity) {
        return userRepository.save(userEntity).getId();
    }

    private void saveUserPhoneInfo(List<UserPhoneDTO> userPhoneDTOList, UUID idUser) {
        userPhoneDTOList.forEach(
                p ->
                        userPhoneRepository.save(
                                UserPhoneEntity.builder()
                                        .idUser(idUser)
                                        .number(p.getNumber())
                                        .cityCode(p.getCityCode())
                                        .countryCode(p.getCountryCode())
                                        .build()));
    }

    private UserResponseDTO generateResponse(UserEntity userEntity) {
        log.debug("creating response...");
        return userEntityToResponse(userEntity);
    }
}
