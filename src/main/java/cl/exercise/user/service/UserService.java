package cl.exercise.user.service;

import static cl.exercise.user.transformer.Transformer.userEntityToResponse;
import static cl.exercise.user.transformer.Transformer.userPhoneEntityToDTO;

import cl.exercise.user.dto.UserDTO;
import cl.exercise.user.dto.UserPhoneDTO;
import cl.exercise.user.dto.UserResponse;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.entities.UserPhoneEntity;
import cl.exercise.user.repository.UserPhoneRepository;
import cl.exercise.user.repository.UserRepository;
import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;
import javax.transaction.Transactional;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

@Service
@Slf4j
public class UserService {

  private final UserRepository userRepository;

  private final UserPhoneRepository userPhoneRepository;

  private final BCryptPasswordEncoder bCryptPasswordEncoder;

  public UserService(
      UserRepository userRepository,
      UserPhoneRepository userPhoneRepository,
      BCryptPasswordEncoder bCryptPasswordEncoder) {
    this.userRepository = userRepository;
    this.userPhoneRepository = userPhoneRepository;
    this.bCryptPasswordEncoder = bCryptPasswordEncoder;
  }

  @SneakyThrows
  public UserResponse saveUser(UserDTO request) {
    log.info("--> saving user {}", request.toString());

    UUID userId =
        saveUserInfo(
            UserEntity.builder()
                .email(request.getUserEmail())
                .name(request.getUserName())
                .password(request.getUserPassword())
                .token(request.getUserToken())
                .isActive(true)
                .build());

    if (!CollectionUtils.isEmpty(request.getUserPhoneDTO())) {
      log.info("--> saving phone {}", request.getUserPhoneDTO().toString());
      saveUserPhoneInfo(request.getUserPhoneDTO(), userId);
    }

    UserEntity userEntity = userRepository.findById(userId).get();
    return generateResponse(userEntity);
  }

  @SneakyThrows
  public UserEntity findUserByEmail(String email) {
    return userRepository.findByEmail(email);
  }

  @SneakyThrows
  public UserDTO getUserInformation(UserDTO request) {
    log.info("--> getting user info {}", request.toString());
    UserEntity userEntity = findUserByEmail(request.getUserEmail());
    List<UserPhoneEntity> userPhoneEntity = userPhoneRepository.findByIdUser(userEntity.getId());

    return userPhoneEntityToDTO(userEntity, userPhoneEntity);
  }

  public UserResponse updateUser(UserDTO request) {
    log.info("--> update user {}", request.toString());
    UserEntity userEntity = findUserByEmail(request.getUserEmail());

    userRepository.updateUserInfo(
        userEntity.getId(),
        request.getUserName(),
        request.getUserEmail(),
        request.getUserPassword(),
        request.getUserToken(),
        Timestamp.from(Instant.now()));

    List<UserPhoneEntity> phoneEntities = userPhoneRepository.findByIdUser(userEntity.getId());
    if (!CollectionUtils.isEmpty(phoneEntities)) {
      log.info("--> delete phone {}", phoneEntities.toString());
      userPhoneRepository.deleteByIdUser(userEntity.getId());
    }
    if (!CollectionUtils.isEmpty(request.getUserPhoneDTO())) {
      log.info("--> saving phone {}", request.getUserPhoneDTO().toString());
      saveUserPhoneInfo(request.getUserPhoneDTO(), userEntity.getId());
    }
    return generateResponse(userRepository.findById(userEntity.getId()).get());
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

  private UserResponse generateResponse(UserEntity userEntity) {
    log.info("creating response...");
    return userEntityToResponse(userEntity);
  }
}
