package matheusko.TQI.Evolution.emprestimo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoIdValorTotalEQuantParcelas {
    private Long id;
    private Double valorTotal;
    private int quantParcelas;
}
