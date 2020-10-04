package cl.exercise.user.exception.validator;

import static cl.exercise.user.exception.validator.ValidatorUtil.isValidText;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class UserPasswordValidator implements ConstraintValidator<UserPasswordConstraint, String> {

  @Override
  public void initialize(UserPasswordConstraint userPasswordConstraint) {}

  @Override
  @SneakyThrows
  public boolean isValid(String password, ConstraintValidatorContext constraintValidatorContext) {
    String regex = "(?=.{2,}[0-9])(?=.+[a-z])(?=.{1,}[A-Z])(?=\\S+$)(?=.*[@#$%^&-+=()]).{8,16}";
    return isValidText(password, regex);
  }
}
