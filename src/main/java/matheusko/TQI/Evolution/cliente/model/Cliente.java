package matheusko.TQI.Evolution.cliente.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import matheusko.TQI.Evolution.emprestimo.model.Emprestimo;
import org.hibernate.validator.constraints.br.CPF;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "cliente")
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false, length = 100)
    @NotBlank(message = "Nome não pode estar vazio.")
    @Pattern(regexp = "^[A-Z]+(.)*", message = "Nome deve começar com maiúcula.")
    private String nome;

    @Column(nullable = false, unique = true, length = 50)
    @Email(message = "Email inválido.")
    private String email;

    @Column(nullable = false, unique = true, length = 11)
    @CPF(message = "CPF inválido.")
    private String cpf;

    @Column(nullable = false, unique = true, length = 15)
    private String rg;

    @Column(nullable = false)
    private Double renda;

    @Column(nullable = false)
    private String senha;

    @Column(nullable = false)
    private String rua;

    @Column(nullable = false)
    private String bairro;

    @Column(nullable = false)
    private String numeroCasa;

    @Column(nullable = false)
    private String cidade;

    @Column(nullable = false)
    private String estado;

    @Column(nullable = false)
    private String pais;

    @Column(nullable = false, length = 8)
    private String cep;

    @Override
    public String toString() {
        return "Cliente{" +
                "nome='" + nome + '\'' +
                ", email='" + email + '\'' +
                ", cpf='" + cpf + '\'' +
                ", rg='" + rg + '\'' +
                ", renda=" + renda +
                ", senha='" + senha + '\'' +
                ", rua='" + rua + '\'' +
                ", bairro='" + bairro + '\'' +
                ", numeroCasa='" + numeroCasa + '\'' +
                ", cidade='" + cidade + '\'' +
                ", estado='" + estado + '\'' +
                ", pais='" + pais + '\'' +
                ", cep='" + cep + '\'' +
                '}';
    }
}
