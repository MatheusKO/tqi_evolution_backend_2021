package matheusko.TQI.Evolution.emprestimo.model.validation.constraints;

import matheusko.TQI.Evolution.emprestimo.model.validation.DataPrimeiraParcelaValidation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = DataPrimeiraParcelaValidation.class)
@Target({ ElementType.METHOD, ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface DataPrimeiraParcela {

    String message() default "Data da primeira parcela é inválida";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
