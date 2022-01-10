package security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import matheusko.TQI.Evolution.cliente.model.Cliente;
import config.AuthenticationConfigConstants;
import data.DetalheClienteData;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

public class JWTAutenticarFilter extends UsernamePasswordAuthenticationFilter {

    private final AuthenticationManager authenticationManager;

    public JWTAutenticarFilter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authResult) throws IOException, ServletException {
        DetalheClienteData clienteData = (DetalheClienteData) authResult.getPrincipal();
        String token = JWT.create()
                .withSubject(clienteData.getUsername())
                .withExpiresAt(new Date(System.currentTimeMillis() + AuthenticationConfigConstants.EXPIRATION_TIME))
                .sign(Algorithm.HMAC512(AuthenticationConfigConstants.SECRET.getBytes()));
        response.addHeader(AuthenticationConfigConstants.HEADER_STRING, AuthenticationConfigConstants.TOKEN_PREFIX + token);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String pair = new String(Base64.decodeBase64(request.getHeader(AuthenticationConfigConstants.HEADER_STRING).substring(6)));
        String email = pair.split(":")[0];
        String senha = pair.split(":")[1];
        Cliente cliente = new Cliente();
        cliente.setEmail(email);
        cliente.setSenha(senha);

        return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(cliente.getEmail(), cliente.getSenha(), new ArrayList<>()));
    }
}
