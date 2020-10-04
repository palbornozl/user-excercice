package cl.exercise.user.transformer;

import cl.exercise.user.dto.UserDTO;
import cl.exercise.user.dto.UserPhoneDTO;
import cl.exercise.user.dto.UserResponse;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.entities.UserPhoneEntity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Transformer {

  public static final String DATE_FORMAT_COMPLETE = "EEEE, MMMM dd, yyyy hh:mm:ss.SSS a";

  public static List<UserPhoneDTO> phoneEntityToResponse(List<UserPhoneEntity> userPhoneEntity) {
    log.debug("--> transform phone response");
    List<UserPhoneDTO> userPhoneDTO = new LinkedList<>();
    userPhoneEntity.forEach(
        p ->
            userPhoneDTO.add(
                UserPhoneDTO.builder()
                    .number(p.getNumber())
                    .cityCode(p.getCountryCode())
                    .countryCode(p.getCountryCode())
                    .build()));

    return userPhoneDTO;
  }

  public static UserResponse userEntityToResponse(UserEntity userEntity) {
    log.debug("--> transform user response");
    return UserResponse.builder()
        .userId(userEntity.getId().toString())
        .createdAt(getDateFormatCompleteToString(new Date(userEntity.getCreatedAt().getTime())))
        .modifiedAt(getDateFormatCompleteToString(new Date(userEntity.getModifiedAt().getTime())))
        .lastLogin(getDateFormatCompleteToString(new Date(userEntity.getLastLogin().getTime())))
        .isActive(userEntity.isActive())
        .userToken(userEntity.getToken())
        .build();
  }

  public static UserDTO userPhoneEntityToDTO(
      UserEntity userEntity, List<UserPhoneEntity> userPhoneEntity) {
    log.debug("--> transform user info DTO");
    return UserDTO.builder()
        .userName(userEntity.getName())
        .userEmail(userEntity.getEmail())
        .userPassword(userEntity.getPassword())
        .userToken(userEntity.getToken())
        .userPhoneDTO(phoneEntityToResponse(userPhoneEntity))
        .build();
  }

  @SneakyThrows
  public static String getDateFormatCompleteToString(Date date) {
    return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(date);
  }
}
