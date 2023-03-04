package cl.exercise.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = UserEmailValidator.class)
@Target({ElementType.METHOD, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
public @interface UserEmailConstraint {

    String message() default "Email con formato incorrecto, favor de revisar";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
