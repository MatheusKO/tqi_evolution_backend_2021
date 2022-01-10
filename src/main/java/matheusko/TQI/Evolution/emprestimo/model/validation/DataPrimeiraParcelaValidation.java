package matheusko.TQI.Evolution.emprestimo.model.validation;

import matheusko.TQI.Evolution.emprestimo.model.validation.constraints.DataPrimeiraParcela;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;

public class DataPrimeiraParcelaValidation implements ConstraintValidator<DataPrimeiraParcela, LocalDate> {

    @Override
    public void initialize(DataPrimeiraParcela constraintAnnotation) {
//        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(LocalDate value, ConstraintValidatorContext context) {
        LocalDate dataMinima = LocalDate.now().plusMonths(3);
        if (value == null || value.isAfter(dataMinima)) {
            return false;
        } else {
            return true;
        }
    }
}
