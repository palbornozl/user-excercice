package cl.exercise.user.transformer;

import cl.exercise.user.dto.UserLoginDTO;
import cl.exercise.user.dto.UserPhoneDTO;
import cl.exercise.user.dto.UserRequestDTO;
import cl.exercise.user.dto.UserResponseDTO;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.entities.UserPhoneEntity;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@Slf4j
public class Transformer {

    public static final String DATE_FORMAT_COMPLETE = "EEEE, MMMM dd, yyyy hh:mm:ss.SSS a";

    Transformer() {
    }

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

    public static UserResponseDTO userEntityToResponse(UserEntity userEntity) {
        log.debug("--> transform user response");
        return UserResponseDTO.builder()
                .userId(userEntity.getId().toString())
                .createdAt(getDateFormatCompleteToString(new Date(userEntity.getCreatedAt().getTime())))
                .modifiedAt(getDateFormatCompleteToString(new Date(userEntity.getModifiedAt().getTime())))
                .lastLogin(getDateFormatCompleteToString(new Date(userEntity.getLastLogin().getTime())))
                .isActive(userEntity.getIsActive())
                .userToken(userEntity.getToken())
                .build();
    }

    public static UserResponseDTO userCompleteInformation(UserResponseDTO responseDTO,
                                                          UserEntity userEntity, List<UserPhoneEntity> userPhoneEntity) {
        log.debug("--> transform user info DTO");
        responseDTO.setUserName(userEntity.getName());
        responseDTO.setUserEmail(userEntity.getEmail());
        responseDTO.setUserPhoneDTO(phoneEntityToResponse(userPhoneEntity));
        return responseDTO;
    }

    @SneakyThrows
    public static String getDateFormatCompleteToString(Date date) {
        return new SimpleDateFormat(DATE_FORMAT_COMPLETE).format(date);
    }

    public static UserRequestDTO userLoginTouserRequest(UserLoginDTO request){
        return UserRequestDTO.builder()
                .userEmail(request.getUserEmail())
                .userPassword(request.getUserPassword())
                .build();
    }
}
