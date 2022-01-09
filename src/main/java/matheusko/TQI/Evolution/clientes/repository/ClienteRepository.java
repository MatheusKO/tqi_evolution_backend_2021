package matheusko.TQI.Evolution.clientes.repository;

import matheusko.TQI.Evolution.clientes.model.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

    public Optional<Cliente> findByEmail(String email);
}
