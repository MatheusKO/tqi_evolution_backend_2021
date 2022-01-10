package matheusko.TQI.Evolution.emprestimo.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import config.AuthenticationConfigConstants;
import matheusko.TQI.Evolution.cliente.model.Cliente;
import matheusko.TQI.Evolution.cliente.repository.ClienteRepository;
import matheusko.TQI.Evolution.emprestimo.model.Emprestimo;
import matheusko.TQI.Evolution.emprestimo.model.EmprestimoDetalhes;
import matheusko.TQI.Evolution.emprestimo.model.EmprestimoIdValorTotalEQuantParcelas;
import matheusko.TQI.Evolution.emprestimo.repository.EmprestimoRepository;
import org.apache.tomcat.util.codec.binary.Base64;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceContext;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/emprestimos")
public class EmprestimoController {

    private final EmprestimoRepository emprestimoRepository;
    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    public EmprestimoController(EmprestimoRepository emprestimoRepository, ClienteRepository clienteRepository, PasswordEncoder encoder) {
        this.emprestimoRepository = emprestimoRepository;
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
    }

    @PostMapping
    public ResponseEntity<Object> saveEmprestimo (@RequestBody @Valid Emprestimo emprestimo, @RequestHeader("Authorization") String authorizationHeader) {
        String pair = new String(Base64.decodeBase64(authorizationHeader.substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];

        Optional<Cliente> optCliente = clienteRepository.findByEmail(email);

        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        } else {
            boolean valid = encoder.matches(senha, optCliente.get().getSenha());

            if (valid) {
                try {
                    emprestimo.setCliente(optCliente.get());
                    Emprestimo savedEmprestimo = emprestimoRepository.save(emprestimo);
                    return ResponseEntity.ok().body(savedEmprestimo);
                } catch (Exception e) {
                    System.err.println(e.getMessage());
                    return ResponseEntity.badRequest().build();
                }
            } {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não autorizado.");
            }
        }
    }

    @GetMapping
    public ResponseEntity<Object> getEmprestimosByClient (@RequestHeader("Authorization") String authorizationHeader) {

        String pair = new String(Base64.decodeBase64(authorizationHeader.substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];

        Optional<Cliente> optCliente = clienteRepository.findByEmail(email);

        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        } else {
            boolean valid = encoder.matches(senha, optCliente.get().getSenha());

            if (valid) {
                Long id = optCliente.get().getId();
                List<EmprestimoIdValorTotalEQuantParcelas> emprestimos = emprestimoRepository.findAllByClienteId(id);
                return ResponseEntity.status(HttpStatus.OK).body(emprestimos);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não autorizado");
            }
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEmprestimoDetail (@RequestHeader("Authorization") String authorizationHeader, @PathVariable("id") Long id) {

        String pair = new String(Base64.decodeBase64(authorizationHeader.substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];

        Optional<Cliente> optCliente = clienteRepository.findByEmail(email);
        Optional<Emprestimo> optEmprestimoCheckId = emprestimoRepository.findById(id);

        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        } else {
            Optional<EmprestimoDetalhes> optEmprestimo = Optional.ofNullable(emprestimoRepository.findEmprestimoById(id));
            if(optEmprestimo.isEmpty()) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Empréstimo não encontrado.");
            } else {
                if (encoder.matches(senha, optCliente.get().getSenha()) && optEmprestimoCheckId.get().getCliente().getId() == optCliente.get().getId()) {
                    return ResponseEntity.status(HttpStatus.OK).body(optEmprestimo.get());
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não autorizado");
                }
            }
        }
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public Map<String, String> handleValidationException(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();

        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();

            errors.put(fieldName, errorMessage);
        });

        return errors;
    }
}
