package es.juanito.institutos.institutos.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class InstitutoCodeValidator implements ConstraintValidator<InstitutoCode, String> {

    @Override
    public void initialize(InstitutoCode constraintAnnotation) {
        // No necesita inicialización especial
    }

    @Override
    public boolean isValid(String code, ConstraintValidatorContext context) {
        if (code == null || code.isEmpty()) {
            return true; // Permitir null, usar @NotBlank si es obligatorio
        }

        // Ejemplo de reglas para el código del instituto:
        // 1. Debe tener formato XXX-YYYY donde XXX son letras y YYYY números
        boolean valid = code.matches("[A-Z]{3}-\\d{4}");
        return valid;
    }
}
