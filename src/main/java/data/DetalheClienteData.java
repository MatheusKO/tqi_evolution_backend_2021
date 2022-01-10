package data;

import matheusko.TQI.Evolution.cliente.model.Cliente;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Optional;

public class DetalheClienteData implements UserDetails {

    private final Optional<Cliente> clienteOptional;

    public DetalheClienteData(Optional<Cliente> clienteOptional) {
        this.clienteOptional = clienteOptional;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return new ArrayList<>();
    }

    @Override
    public String getPassword() {
        return clienteOptional.orElse(new Cliente()).getSenha();
    }

    @Override
    public String getUsername() {
        return clienteOptional.orElse(new Cliente()).getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
