package projeto_integrador.estacionamento.service;

import lombok.RequiredArgsConstructor;
import projeto_integrador.estacionamento.DTO.UsuarioCadastroDTO;
import projeto_integrador.estacionamento.DTO.UsuarioEdicaoDTO;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;

@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;


    // Injeção de dependência via construtor
    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional
    public Usuario cadastrarUsuario(UsuarioCadastroDTO dto) {

        // 1. Verificação de Duplicidade
        if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("Email já cadastrado.");
        }

        if (usuarioRepository.findByCpf(dto.getCpf()).isPresent()) {
            throw new IllegalArgumentException("CPF já cadastrado.");
        }

        // 2. Criptografar Senha
        String senhaCriptografada = passwordEncoder.encode(dto.getSenha());

        // 3. Mapear DTO para Entity
        Usuario novoUsuario = new Usuario(
                dto.getNome(),
                dto.getEmail(),
                dto.getCpf(),
                dto.getCep(),
                dto.getTelefone(),
                dto.getEndereco(),
                senhaCriptografada
        );

        return usuarioRepository.save(novoUsuario);
    }

    public Usuario buscarPorEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
    }

    public Usuario editarUsuario(Long usuarioId, UsuarioEdicaoDTO dto) {

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getEmail() != null) usuario.setEmail(dto.getEmail());
        if (dto.getTelefone() != null) usuario.setTelefone(dto.getTelefone());
        if (dto.getEndereco() != null) usuario.setEndereco(dto.getEndereco());
        if (dto.getCep() != null) usuario.setCep(dto.getCep());
        if (dto.getCpf() != null) usuario.setCpf(dto.getCpf());


        return usuarioRepository.save(usuario);
    }

    public List<Usuario> listarTodos() {
        return usuarioRepository.findAll();
    }

    public Usuario editarUsuarioComoAdmin(Long id, UsuarioEdicaoDTO dto) {

        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        // Evitar trocar email para um já existente
        if (dto.getEmail() != null && !dto.getEmail().equals(usuario.getEmail())) {
            if (usuarioRepository.findByEmail(dto.getEmail()).isPresent()) {
                throw new IllegalArgumentException("Email já está em uso.");
            }
            usuario.setEmail(dto.getEmail());
        }

        if (dto.getNome() != null) usuario.setNome(dto.getNome());
        if (dto.getTelefone() != null) usuario.setTelefone(dto.getTelefone());
        if (dto.getEndereco() != null) usuario.setEndereco(dto.getEndereco());
        if (dto.getCep() != null) usuario.setCep(dto.getCep());
        if (dto.getCpf() != null) usuario.setCpf(dto.getCpf());

        return usuarioRepository.save(usuario);
    }

    public void excluirUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuário não encontrado");
        }

        usuarioRepository.deleteById(id);
    }

}