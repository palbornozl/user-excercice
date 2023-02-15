package cl.exercise.user.exception.validator;

import static cl.exercise.user.exception.validator.ValidatorUtil.isValidText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserEmailValidator implements ConstraintValidator<UserEmailConstraint, String> {

  @Override
  public void initialize(UserEmailConstraint userEmailConstraint) {
    // nothing to do here...
  }

  @Override
  @SneakyThrows
  public boolean isValid(String email, ConstraintValidatorContext constraintValidatorContext) {
    String regex =
        "^(?=.{5,100}$)^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    return isValidText(email, regex);
  }
}
