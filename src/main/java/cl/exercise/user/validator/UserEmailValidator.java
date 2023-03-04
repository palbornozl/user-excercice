package cl.exercise.user.validator;

import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

@Slf4j
public class UserEmailValidator implements ConstraintValidator<UserEmailConstraint, String> {

    @Value("${vault.my.email.regex}")
    private String regex;

    @Override
    public void initialize(UserEmailConstraint userEmailConstraint) {
        // nothing to do here...
    }

    @Override
    @SneakyThrows
    public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
        log.debug(">> regex {}", regex);
        return ValidatorUtil.isValidText(email, regex);
    }
}
