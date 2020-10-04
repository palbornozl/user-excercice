package cl.exercise.user.exception.validator;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;

@Slf4j
public class ValidatorUtil {

  @SneakyThrows
  public static boolean isValidText(String text, String regex) {
    if (StringUtils.isEmpty(text)) return false;
    try {
      Pattern pattern = Pattern.compile(regex);
      Matcher matcher = pattern.matcher(text);
      return matcher.find();
    } catch (Exception e) {
      log.error("Error con el formato de email");
      return false;
    }
  }
}
