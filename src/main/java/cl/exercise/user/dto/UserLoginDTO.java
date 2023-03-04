package cl.exercise.user.dto;

import cl.exercise.user.validator.UserEmailConstraint;
import cl.exercise.user.validator.UserPasswordConstraint;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserLoginDTOBuilder", toBuilder = true)
@JsonDeserialize(builder = UserLoginDTO.UserLoginDTOBuilder.class)
public class UserLoginDTO {
    @JsonProperty("email")
    @Size(min = 6, max = 100, message = "Largo no aceptado, minimo 6 y maximo 100 letras")
    @UserEmailConstraint
    @NonNull
    private String userEmail;

    @JsonProperty("password")
    @UserPasswordConstraint
    @NonNull
    private String userPassword;
}
