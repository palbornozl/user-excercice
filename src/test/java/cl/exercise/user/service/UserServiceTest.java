package cl.exercise.user.service;

import static cl.exercise.user.utils.Utils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

import cl.exercise.user.dto.UserDTO;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

@ActiveProfiles("test")
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest
public class UserServiceTest {

  @Mock UserRepository mockRepository;
  File file;
  private UserDTO userDTOSignUpRequest;
  private UUID userId;
  private UserEntity userEntityResponse;

  @Before
  public void setUp() throws IOException {
    ClassLoader classLoader = getClass().getClassLoader();
    JsonNode jsonNode;

    jsonNode = readFile("signUpRequest", classLoader);
    userDTOSignUpRequest = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserDTO.class);
    userDTOSignUpRequest.setUserToken(jsonNode.get("password").asText());

    jsonNode = readFile("userEntityResponse", classLoader);
    userEntityResponse = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserEntity.class);

    userId = UUID.fromString("ba9ac26d-af97-46f4-a4ec-711a99bc4e04");
  }

  @Test
  public void shouldSave() {

    UserEntity userEntity =
        UserEntity.builder()
            .email(userDTOSignUpRequest.getUserEmail())
            .name(userDTOSignUpRequest.getUserName())
            .password(userDTOSignUpRequest.getUserPassword())
            .token(userDTOSignUpRequest.getUserToken())
            .isActive(true)
            .build();

    Mockito.when(mockRepository.save(userEntity)).thenReturn(userEntityResponse);
    assertEquals(userId, userEntityResponse.getId());
  }
}
