package cl.exercise.user.dto;

import cl.exercise.user.exception.validator.UserEmailConstraint;
import cl.exercise.user.exception.validator.UserPasswordConstraint;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonFormat.Shape;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import java.util.List;
import javax.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import org.springframework.lang.NonNull;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserDTOBuilder", toBuilder = true)
@JsonDeserialize(builder = UserDTO.UserDTOBuilder.class)
public class UserDTO {

  @JsonProperty("phones")
  List<UserPhoneDTO> userPhoneDTO;

  @JsonProperty("name")
  @JsonFormat(shape = Shape.STRING)
  @Size(min = 2, max = 250, message = "Largo no aceptado, minimo 3 y maximo 250 letras")
  @NonNull
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

  @JsonProperty("token")
  private String userToken;
}
