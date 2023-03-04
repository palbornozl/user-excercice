package cl.exercise.user.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Slf4j
public class ValidatorUtil {

    ValidatorUtil() {
    }

    @SneakyThrows
    public static boolean isValidText(String text, String regex) {
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
