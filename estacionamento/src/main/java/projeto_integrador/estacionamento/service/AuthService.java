package projeto_integrador.estacionamento.service;

import projeto_integrador.estacionamento.DTO.LoginRequestDTO;
import projeto_integrador.estacionamento.DTO.LoginResponseDTO;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
public class AuthService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    public LoginResponseDTO autenticar(LoginRequestDTO request) {
        Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new NoSuchElementException("Email ou senha inválidos."));

        if (!passwordEncoder.matches(request.getSenha(), usuario.getSenhaHash())) {
            throw new IllegalArgumentException("Email ou senha inválidos.");
        }

        // 🔥 Token agora contém ID
        String jwtToken = jwtService.generateToken(usuario);

        return new LoginResponseDTO(jwtToken, usuario.getNome(), usuario.getEmail());
    }
}
