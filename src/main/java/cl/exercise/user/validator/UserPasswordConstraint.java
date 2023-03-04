package cl.exercise.user.validator;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

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
