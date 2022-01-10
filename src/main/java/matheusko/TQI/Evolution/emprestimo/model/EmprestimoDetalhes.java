package matheusko.TQI.Evolution.emprestimo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class EmprestimoDetalhes {
    private Long id;
    private Double valorTotal;
    private int quantParcelas;
    private LocalDate primeiraParcela;
    private String clienteEmail;
    private Double clienteRenda;
}
