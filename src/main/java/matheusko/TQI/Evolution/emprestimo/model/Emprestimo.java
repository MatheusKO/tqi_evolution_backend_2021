package matheusko.TQI.Evolution.emprestimo.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import matheusko.TQI.Evolution.cliente.model.Cliente;
import matheusko.TQI.Evolution.emprestimo.model.validation.constraints.DataPrimeiraParcela;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "emprestimo")
public class Emprestimo {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(nullable = false)
    private Double valorTotal;

    @Column(nullable = false)
    @Min(value = 1, message = "Número mínimo de parcelas é 1.")
    @Max(value = 60, message = "Número máximo de parcelas é 60")
    private int quantParcelas;

    @Column(nullable = false)
    @DataPrimeiraParcela
    private LocalDate primeiraParcela;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @NotNull
    private Cliente cliente;

}
