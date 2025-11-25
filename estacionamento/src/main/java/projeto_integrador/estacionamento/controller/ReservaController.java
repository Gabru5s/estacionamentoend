package projeto_integrador.estacionamento.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_integrador.estacionamento.DTO.ReservaCreateDTO;
import projeto_integrador.estacionamento.DTO.ReservaRequestDTO;
import projeto_integrador.estacionamento.entity.Reserva;
import projeto_integrador.estacionamento.service.JwtService;
import projeto_integrador.estacionamento.service.ReservaService;


import java.util.List;

@RestController
@RequestMapping("/api/reservas")
public class ReservaController {

    private final ReservaService reservaService;
    private final JwtService jwtService;

    public ReservaController(ReservaService reservaService, JwtService jwtService) {
        this.reservaService = reservaService;
        this.jwtService = jwtService;
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

    //Criação de reserva autenticada
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

    //Listar reservas do usuário logado
    @GetMapping("/minhas")
    public ResponseEntity<List<Reserva>> listarMinhasReservas(HttpServletRequest request) {
        String token = request.getHeader("Authorization").replace("Bearer ", "");
        Long usuarioId = jwtService.extrairUserId(token);

        List<Reserva> reservas = reservaService.listarPorUsuario(usuarioId);
        return ResponseEntity.ok(reservas);
    }
}
