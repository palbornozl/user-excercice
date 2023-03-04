package cl.exercise.user.dto;

import cl.exercise.user.validator.UserEmailConstraint;
import cl.exercise.user.validator.UserPasswordConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Size;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserRequestDTOBuilder", toBuilder = true)
@JsonDeserialize(builder = UserRequestDTO.UserRequestDTOBuilder.class)
public class UserRequestDTO {

    @JsonProperty("name")
    @JsonFormat(shape = Shape.STRING)
    @Size(min = 2, max = 250, message = "Largo no aceptado, minimo 3 y maximo 250 letras")
    private String userName;
    @JsonProperty("email")
    @Size(min = 6, max = 100, message = "Largo no aceptado, minimo 6 y maximo 100 letras")
    @UserEmailConstraint
    @NonNull
    private String userEmail;
    @JsonProperty("password")
    @UserPasswordConstraint
    @NonNull
    private String userPassword;
    @JsonProperty("phones")
    private List<UserPhoneDTO> userPhoneDTO;

}
