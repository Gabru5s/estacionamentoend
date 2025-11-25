package projeto_integrador.estacionamento.DTO;

import java.time.LocalDateTime;

public record ReservaRequestDTO(
        Long usuarioId,
        Long vagaId,
        LocalDateTime inicio,
        LocalDateTime fim
) { }
