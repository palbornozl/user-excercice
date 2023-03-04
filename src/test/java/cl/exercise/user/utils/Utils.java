package cl.exercise.user.utils;

import cl.exercise.user.dto.UserPhoneDTO;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Utils {

  public static <T> T mapFromJson(String json, Class<T> clazz) throws IOException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.readValue(json, clazz);
  }

  public static String mapToJson(Object obj) throws JsonProcessingException {
    ObjectMapper objectMapper = new ObjectMapper();
    return objectMapper.writeValueAsString(obj);
  }

  public static JsonNode removeUserPhoneDTO(File file) throws IOException {
    JsonNode jsonNode = new ObjectMapper().readTree(file);
    List<UserPhoneDTO> userPhoneDTOSignInRequest =
        new ObjectMapper()
            .convertValue(jsonNode.get("phones"), new TypeReference<List<UserPhoneDTO>>() {});

    ((ObjectNode) jsonNode).remove(Collections.singletonList("phones"));
    return jsonNode;
  }

  public static JsonNode readFile(String fileName, ClassLoader classLoader) throws IOException{
    File file = new File(
        Objects.requireNonNull(classLoader.getResource("json/" + fileName + ".json")).getFile());
    return removeUserPhoneDTO(file);
  }
}
