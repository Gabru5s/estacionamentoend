package projeto_integrador.estacionamento.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_integrador.estacionamento.DTO.ReservaChatbotDTO;
import projeto_integrador.estacionamento.DTO.ReservaCreateDTO;
import projeto_integrador.estacionamento.DTO.ReservaRequestDTO;
import projeto_integrador.estacionamento.entity.Reserva;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import projeto_integrador.estacionamento.service.JwtService;
import projeto_integrador.estacionamento.service.ReservaService;

import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final JwtService jwtService;
    private final UsuarioRepository usuarioRepository;

    public ReservaController(ReservaService reservaService,
                             JwtService jwtService,
                             UsuarioRepository usuarioRepository) {
        this.reservaService = reservaService;
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping
    public ResponseEntity<?> criar(@RequestBody ReservaRequestDTO dto) {
        try {
            Reserva reserva = reservaService.criar(dto);
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    @GetMapping("/usuario/{usuarioId}")
    public ResponseEntity<List<Reserva>> listarPorUsuario(@PathVariable Long usuarioId) {
        List<Reserva> reservas = reservaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(reservas);
    }

    @GetMapping
    public ResponseEntity<List<Reserva>> listarTodas() {
        List<Reserva> reservas = reservaService.listarTodas();
        return ResponseEntity.ok(reservas);
    }

    // Criação de reserva autenticada (com JWT) - usada pelo front web
    @PostMapping("/minhas")
    public ResponseEntity<?> criarMinhaReserva(
            @RequestBody ReservaCreateDTO dto,
            HttpServletRequest request
    ) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long usuarioId = jwtService.extrairUserId(token);

        Reserva reserva = reservaService.criarParaUsuario(usuarioId, dto);
        return new ResponseEntity<>(reserva, HttpStatus.CREATED);
    }

    // Listar reservas do usuário logado (com JWT)
    @GetMapping("/minhas")
    public ResponseEntity<List<Reserva>> listarMinhasReservas(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long usuarioId = jwtService.extrairUserId(token);

        List<Reserva> reservas = reservaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(reservas);
    }

    //Criação via chatbot (telefone + categoria)
    @PostMapping("/chatbot")
    public ResponseEntity<?> criarReservaViaChatbot(@RequestBody ReservaChatbotDTO dto) {
        try {
            Reserva reserva = reservaService.criarViaChatbot(dto);
            return new ResponseEntity<>(reserva, HttpStatus.CREATED);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //Listar reservas via WhatsApp (sem JWT, usa telefone)
    @GetMapping("/whatsapp/minhas")
    public ResponseEntity<?> listarMinhasReservasWhatsapp(@RequestParam String telefone) {

        // Normaliza igual ao bot (defensivo)
        String tel = telefone;
        if (tel.startsWith("+55")) {
            tel = tel.substring(3);
        } else if (tel.startsWith("55")) {
            tel = tel.substring(2);
        }

        Usuario usuario = usuarioRepository.findByTelefone(tel)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado para este telefone"));

        List<Reserva> reservas = reservaService.listarPorUsuario(usuario.getId());
        return ResponseEntity.ok(reservas);
    }
}
