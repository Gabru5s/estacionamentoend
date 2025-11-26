package projeto_integrador.estacionamento.controller;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_integrador.estacionamento.DTO.*;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.service.AuthService;
import projeto_integrador.estacionamento.service.JwtService;
import projeto_integrador.estacionamento.service.UsuarioService;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/usuarios")
@CrossOrigin(origins = "http://localhost:4200")
public class UsuarioController {

    private final UsuarioService usuarioService;
    private final AuthService authService;
    private final JwtService jwtService;

    public UsuarioController(UsuarioService usuarioService,
                             AuthService authService,
                             JwtService jwtService) {
        this.usuarioService = usuarioService;
        this.authService = authService;
        this.jwtService = jwtService;
    }

    @PostMapping("/cadastro")
    public ResponseEntity<String> cadastrar(@RequestBody @Valid UsuarioCadastroDTO dto) {
        try {
            Usuario usuarioSalvo = usuarioService.cadastrarUsuario(dto);
            return new ResponseEntity<>("Usuário cadastrado com sucesso!", HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro interno ao tentar cadastrar o usuário.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody @Valid LoginRequestDTO request) {
        try {
            LoginResponseDTO response = authService.autenticar(request);
            return ResponseEntity.ok(response);
        } catch (NoSuchElementException | IllegalArgumentException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Erro interno ao tentar fazer login.",
                    HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    // /api/usuarios/me?email=...
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
    public Usuario editarUsuario(@RequestBody UsuarioEdicaoDTO dto,
                                 HttpServletRequest request) {
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
    public ResponseEntity<Usuario> editarPorId(@PathVariable Long id,
                                               @RequestBody UsuarioEdicaoDTO dto) {
        Usuario atualizado = usuarioService.editarUsuarioComoAdmin(id, dto);
        return ResponseEntity.ok(atualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> excluir(@PathVariable Long id) {
        usuarioService.excluirUsuario(id);
        return ResponseEntity.noContent().build();
    }
}
