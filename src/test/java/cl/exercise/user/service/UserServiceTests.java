package cl.exercise.user.service;

import cl.exercise.user.dto.UserRequestDTO;
import cl.exercise.user.dto.UserResponseDTO;
import cl.exercise.user.entities.UserEntity;
import cl.exercise.user.entities.UserPhoneEntity;
import cl.exercise.user.repository.UserPhoneRepository;
import cl.exercise.user.repository.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static cl.exercise.user.utils.Utils.readFile;
import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@SpringBootTest
class UserServiceTests {
    @Mock
    UserRepository mockUserRepository;
    @Mock
    UserPhoneRepository mockUserPhoneRepository;
    List<UserPhoneEntity> userPhoneEntity;
    private UserRequestDTO userRequestDTO;
    private UserResponseDTO userResponseDTO;
    private UUID userId;
    private UserEntity userEntityResponse;
    private UserEntity userEntityResponseGet;

    @BeforeEach
    public void setUp() throws IOException {
        ClassLoader classLoader = getClass().getClassLoader();

        JsonNode jsonNode = readFile("signUpRequest", classLoader);
        userRequestDTO = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserRequestDTO.class);
        jsonNode = readFile("signUpResponse", classLoader);
        userEntityResponse = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserEntity.class);
        userId = UUID.fromString("275d2e63-978f-43bb-a740-e5fbf1cd55b0");

        jsonNode = readFile("getUserEntity", classLoader);
        userEntityResponseGet = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserEntity.class);
        jsonNode = readFile("getUserPhoneEntity", classLoader);
        userPhoneEntity = new ObjectMapper().readValue(jsonNode.toPrettyString(), new TypeReference<List<UserPhoneEntity>>() {
        });
        jsonNode = readFile("getUserResponse", classLoader);
        userResponseDTO = new ObjectMapper().readValue(jsonNode.toPrettyString(), UserResponseDTO.class);
    }

    @Test
    void testSignUp() {
        UserEntity userEntity =
                UserEntity.builder()
                        .email(userRequestDTO.getUserEmail())
                        .name(userRequestDTO.getUserName())
                        .password(userRequestDTO.getUserPassword())
                        .isActive(true)
                        .build();

        Mockito.when(mockUserRepository.save(userEntity)).thenReturn(userEntityResponse);
        assertEquals(userId, userEntityResponse.getId());
    }

    @Test
    void testGetUserCompleteInformation() {
        String email = "juan@rodriguezdorg.cl";

        Mockito.when(mockUserRepository.findByEmail(email)).thenReturn(Optional.ofNullable(userEntityResponseGet));
        Mockito.when(mockUserPhoneRepository.findByIdUser(userEntityResponseGet.getId())).thenReturn(userPhoneEntity);

        assertEquals("c8a222b6-503e-4428-a705-d1d7d81808d0", userResponseDTO.getUserId());
        assertEquals(email, userResponseDTO.getUserEmail());
        assertEquals(1, userResponseDTO.getUserPhoneDTO().size());

    }
}
