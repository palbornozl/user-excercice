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
  public static JsonNode readFile(String fileName, ClassLoader classLoader) throws IOException{
    File file = new File(
        Objects.requireNonNull(classLoader.getResource("json/" + fileName + ".json")).getFile());
    return new ObjectMapper().readTree(file);
  }
}
