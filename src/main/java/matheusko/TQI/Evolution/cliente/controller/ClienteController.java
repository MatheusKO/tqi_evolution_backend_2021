package matheusko.TQI.Evolution.cliente.controller;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import matheusko.TQI.Evolution.cliente.model.Cliente;
import matheusko.TQI.Evolution.cliente.repository.ClienteRepository;
import config.AuthenticationConfigConstants;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.MultiValueMap;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import javax.persistence.Tuple;
import javax.validation.Valid;
import java.util.*;

@RestController
@RequestMapping("/api/v1/clientes")
public class ClienteController {

    private final ClienteRepository clienteRepository;
    private final PasswordEncoder encoder;

    public ClienteController(final ClienteRepository clienteRepository, PasswordEncoder encoder) {
        this.clienteRepository = clienteRepository;
        this.encoder = encoder;
    }

    @GetMapping
    public ResponseEntity<List<Cliente>> getClientes() {
        return ResponseEntity.ok(clienteRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<Cliente> saveCliente(@RequestBody @Valid Cliente cliente) {
        try {
            cliente.setSenha(encoder.encode(cliente.getSenha()));
            Cliente savedCliente = clienteRepository.save(cliente);
            return ResponseEntity.ok().body(savedCliente);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            return ResponseEntity.badRequest().build();
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

    @GetMapping("/login")
    public ResponseEntity<Object> logarCliente(@RequestHeader("Authorization") String authorizationHeader) {

        String pair = new String(Base64.decodeBase64(authorizationHeader.substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];

        Optional<Cliente> optCliente = clienteRepository.findByEmail(email);

        if (optCliente.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Cliente não encontrado.");
        } else {
            boolean valid = encoder.matches(senha, optCliente.get().getSenha());

            if (valid) {
                String token = JWT.create()
                        .withSubject(email)
                        .withExpiresAt(new Date(System.currentTimeMillis() + AuthenticationConfigConstants.EXPIRATION_TIME))
                        .sign(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()));
                MultiValueMap<String, String> headers = new HttpHeaders();
                headers.add(AuthenticationConfigConstants.HEADER_STRING, AuthenticationConfigConstants.TOKEN_PREFIX + token);
                return new ResponseEntity(headers, HttpStatus.OK);
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Cliente não autorizado.");
            }

        }
    }
}
