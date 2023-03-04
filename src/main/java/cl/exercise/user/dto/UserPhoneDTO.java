package cl.exercise.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import org.springframework.lang.NonNull;

import javax.validation.constraints.Min;
import javax.validation.constraints.Size;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserPhoneDTOBuilder", toBuilder = true)
@JsonDeserialize(builder = UserPhoneDTO.UserPhoneDTOBuilder.class)
public class UserPhoneDTO {

    @Min(value = 100, message = "Numero de telefono debe ser mayor a 99")
    @Size(min = 3, max = 16, message = "Largo no aceptado, minimo 3 y maximo 16 digitos")
    @NonNull
    @JsonProperty("number")
    private Integer number;

    @Min(value = 1, message = "Codigo de ciudad debe ser mayor a cero")
    @Size(min = 1, max = 4, message = "Largo no aceptado, minimo 1 y maximo 4 digitos")
    @NonNull
    @JsonProperty("citycode")
    private Integer cityCode;

    @Min(value = 1, message = "Codigo de pais debe ser mayor a cero")
    @Size(min = 1, max = 4, message = "Largo no aceptado, minimo 1 y maximo 4 digitos")
    @NonNull
    @JsonProperty("countrycode")
    private Integer countryCode;
}
