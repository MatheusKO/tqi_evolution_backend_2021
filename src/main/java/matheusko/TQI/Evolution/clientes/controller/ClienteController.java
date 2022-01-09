package matheusko.TQI.Evolution.clientes.controller;

import matheusko.TQI.Evolution.clientes.model.Cliente;
import matheusko.TQI.Evolution.clientes.repository.ClienteRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    public ClienteController(final ClienteRepository clienteRepository, PasswordEncoder encoder) {
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
    }

//    @GetMapping
//    public ResponseEntity<List<Cliente>> getClientes() {
//        return ResponseEntity.ok(clienteRepository.findAll());
//    }

    @PostMapping
    public ResponseEntity<Cliente> saveCliente(@RequestBody Cliente cliente) {
        try {
            cliente.setSenha(encoder.encode(cliente.getSenha()));
            Cliente savedCliente = clienteRepository.save(cliente);
            return ResponseEntity.ok().body(savedCliente);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/login")
    public ResponseEntity<Boolean> logarCliente(@RequestHeader("Authorization") String authorizationHeader) {

        String pair = new String(Base64.decodeBase64(authorizationHeader.substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];

        Optional<Cliente> optCliente = clienteRepository.findByEmail(email);

        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(false);
        } else {
            boolean valid = encoder.matches(senha, optCliente.get().getSenha());

            HttpStatus status = (valid) ? HttpStatus.OK : HttpStatus.UNAUTHORIZED;

            return ResponseEntity.status(status).body(valid);
        }
    }
}
