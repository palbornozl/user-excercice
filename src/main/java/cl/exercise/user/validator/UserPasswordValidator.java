package cl.exercise.user.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import static cl.exercise.user.validator.ValidatorUtil.isValidText;

@Slf4j
public class UserPasswordValidator implements ConstraintValidator<UserPasswordConstraint, String> {

    @Value("${vault.my.password.regex}")
    private String regex;

    @Override
    public void initialize(UserPasswordConstraint userPasswordConstraint) {
        // nothing to do here...
    }

    @Override
    @SneakyThrows
    public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
        log.debug(">> regex {}", regex);
        return isValidText(password, regex);
    }
}
