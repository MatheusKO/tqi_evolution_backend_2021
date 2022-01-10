package services;

import matheusko.TQI.Evolution.cliente.model.Cliente;
import matheusko.TQI.Evolution.cliente.repository.ClienteRepository;
import data.DetalheClienteData;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class DetalheClienteServiceImpl implements UserDetailsService {

    private final ClienteRepository clienteRepository;

    public DetalheClienteServiceImpl(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Optional<Cliente> clienteOptional = clienteRepository.findByEmail(email);

        if(clienteOptional.isEmpty()) {
            throw new UsernameNotFoundException("Email [" + email + "] não encontrado.");
        }

        return new DetalheClienteData(clienteOptional);
    }
}
