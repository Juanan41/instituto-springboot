package es.juanito.institutos.institutos.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = InstitutoCodeValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InstitutoCode {
    String message() default "El código del instituto no es válido";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}

