package cl.exercise.user.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;

import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserResponseDTOBuilder", toBuilder = true)
@JsonDeserialize(builder = UserResponseDTO.UserResponseDTOBuilder.class)
public class UserResponseDTO {
    @JsonProperty("phones")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    List<UserPhoneDTO> userPhoneDTO;
    @JsonProperty("id")
    private String userId;
    @JsonProperty("isactive")
    private Boolean isActive;
    @JsonProperty("token")
    private String userToken;
    @JsonProperty("created")
    private String createdAt;
    @JsonProperty("modified")
    private String modifiedAt;
    @JsonProperty("last_login")
    private String lastLogin;
    @JsonProperty("name")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userName;
    @JsonProperty("email")
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String userEmail;

}
