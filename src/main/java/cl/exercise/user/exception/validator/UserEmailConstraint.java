package cl.exercise.user.exception.validator;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import javax.validation.Constraint;
import javax.validation.Payload;

@Documented
@Constraint(validatedBy = UserEmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailConstraint {

  String message() default "Email con formato incorrecto, favor de revisar";

  Class<?>[] groups() default {};

  Class<? extends Payload>[] payload() default {};
}
