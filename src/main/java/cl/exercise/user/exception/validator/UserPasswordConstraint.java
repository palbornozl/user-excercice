package cl.exercise.user.exception.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UserPasswordValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserPasswordConstraint {

  String message() default
      "Password con problemas, minimo 1 letra mayuscula, minusculas, 2 numeros, 1 un simbolo [@#$%^&-+=()]";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
