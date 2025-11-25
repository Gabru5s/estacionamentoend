package projeto_integrador.estacionamento.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_integrador.estacionamento.DTO.VeiculoCadastroDTO;
import projeto_integrador.estacionamento.entity.Veiculo;
import projeto_integrador.estacionamento.repository.VeiculoRepository;
import projeto_integrador.estacionamento.service.JwtService;
import projeto_integrador.estacionamento.service.VeiculoService;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequestMapping("/veiculos")
public class VeiculoController {

    private final VeiculoService veiculoService;
    private final JwtService jwtService;
    private final VeiculoRepository veiculoRepository;

    public VeiculoController(VeiculoService veiculoService,
                             JwtService jwtService,
                             VeiculoRepository veiculoRepository) {
        this.veiculoService = veiculoService;
        this.jwtService = jwtService;
        this.veiculoRepository = veiculoRepository;
    }

    private Long getUsuarioId(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        return jwtService.extrairUserId(token);
    }

    @PostMapping("/cadastrar")
    public Veiculo cadastrar(@RequestBody VeiculoCadastroDTO dto, HttpServletRequest request) {
        Long usuarioId = getUsuarioId(request);
        return veiculoService.cadastrarVeiculo(usuarioId, dto);
    }

    @GetMapping("/meus-veiculos")
    public List<Veiculo> listar(HttpServletRequest request) {
        Long usuarioId = getUsuarioId(request);
        return veiculoRepository.findByUsuarioId(usuarioId);
    }

    @PutMapping("/{id}")
    public Veiculo editar(@PathVariable Long id, @RequestBody VeiculoCadastroDTO dto, HttpServletRequest request) {
        Long usuarioId = getUsuarioId(request);
        return veiculoService.editarVeiculo(id, usuarioId, dto);
    }

    @DeleteMapping("/{id}")
    public void excluir(@PathVariable Long id, HttpServletRequest request) {
        Long usuarioId = getUsuarioId(request);
        veiculoService.excluirVeiculo(id, usuarioId);
    }

    @GetMapping("/lista")
    public ResponseEntity<List<Veiculo>> listarTodos() {
        List<Veiculo> veiculos = veiculoService.listarTodos();
        return ResponseEntity.ok(veiculos);
    }
}
