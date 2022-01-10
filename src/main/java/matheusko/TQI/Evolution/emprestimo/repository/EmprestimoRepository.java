package matheusko.TQI.Evolution.emprestimo.repository;

import com.fasterxml.jackson.databind.util.JSONPObject;
import matheusko.TQI.Evolution.emprestimo.model.Emprestimo;
import matheusko.TQI.Evolution.emprestimo.model.EmprestimoDetalhes;
import matheusko.TQI.Evolution.emprestimo.model.EmprestimoIdValorTotalEQuantParcelas;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EmprestimoRepository extends JpaRepository<Emprestimo, Long> {

//    List<Emprestimo> findByClienteId(@Param("cliente_id") Long id);

    @Query("SELECT new matheusko.TQI.Evolution.emprestimo.model.EmprestimoIdValorTotalEQuantParcelas(e.id, e.valorTotal, e.quantParcelas) FROM emprestimo e WHERE e.cliente.id = :id ")
    List<EmprestimoIdValorTotalEQuantParcelas> findAllByClienteId(@Param("id") Long id);

    @Query("SELECT new matheusko.TQI.Evolution.emprestimo.model.EmprestimoDetalhes(e.id, e.valorTotal, e.quantParcelas, e.primeiraParcela, c.email, c.renda) FROM emprestimo e INNER JOIN e.cliente c WHERE e.id = :id ")
    EmprestimoDetalhes findEmprestimoById(@Param("id") Long id);
}
