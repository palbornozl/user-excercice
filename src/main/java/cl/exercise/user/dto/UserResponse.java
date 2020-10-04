package cl.exercise.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@ToString
@Builder(builderClassName = "UserResponseBuilder", toBuilder = true)
@JsonDeserialize(builder = UserResponse.UserResponseBuilder.class)
public class UserResponse {

  @JsonProperty("id")
  private String userId;

  @JsonProperty("isactive")
  private boolean isActive;

  @JsonProperty("token")
  private String userToken;

  @JsonProperty("created")
  private String createdAt;

  @JsonProperty("modified")
  private String modifiedAt;

  @JsonProperty("last_login")
  private String lastLogin;
}
