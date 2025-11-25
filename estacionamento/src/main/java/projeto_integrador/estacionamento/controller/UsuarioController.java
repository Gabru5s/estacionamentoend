package projeto_integrador.estacionamento.controller;

import jakarta.servlet.http.HttpServletRequest;
import projeto_integrador.estacionamento.DTO.*;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import projeto_integrador.estacionamento.service.AuthService;
import projeto_integrador.estacionamento.service.JwtService;
import projeto_integrador.estacionamento.service.UsuarioService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuarios")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final JwtService jwtService;


    // Injeção de dependência do Service
    public UsuarioController(UsuarioService usuarioService, AuthService authService, JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
        this.jwtService = jwtService;
    }


    @PostMapping("/cadastro")
    // @Valid garante que as anotações do DTO sejam verificadas
    public ResponseEntity<String> cadastrar(@RequestBody @Valid UsuarioCadastroDTO dto) {
        try {
            Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dto);
            // Retorna 201 Created com a mensagem de sucesso para o frontend
            return new ResponseEntity<>("Usuário " + " cadastrado com sucesso!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            // Retorna 400 Bad Request para erros de regra de negócio (Email/CPF duplicado)
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // Logar o erro aqui
            return new ResponseEntity<>("Erro interno ao tentar cadastrar o usuário.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    /**
     * Endpoint para autenticar o usuário e retornar um token JWT.
     */
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.autenticar(request);
            // 200 OK com o token e dados do usuário
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            // Retorna 400 Bad Request para credenciais inválidas
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            // 500 Internal Server Error
            return new ResponseEntity<>("Erro interno ao tentar fazer login.", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/me")
    public UsuarioDTO getUsuarioLogado(@RequestParam String email) {
        Usuario usuario = usuarioService.buscarPorEmail(email);
        return new UsuarioDTO(
                usuario.getNome(),
                usuario.getEmail(),
                usuario.getCpf(),
                usuario.getCep(),
                usuario.getTelefone(),
                usuario.getEndereco()
        );
    }
    @PutMapping("/editar")
    public Usuario editarUsuario(
            @RequestBody UsuarioEdicaoDTO dto,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long usuarioId = jwtService.extrairUserId(token);

        return usuarioService.editarUsuario(usuarioId, dto);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Usuario>> listarTodos() {
        List<Usuario> usuarios = usuarioService.listarTodos();
        return ResponseEntity.ok(usuarios);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Usuario> editarPorId(
            @PathVariable Long id,
            @RequestBody UsuarioEdicaoDTO dto
    ) {
        Usuario atualizado = usuarioService.editarUsuarioComoAdmin(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build(); // 204
    }

}