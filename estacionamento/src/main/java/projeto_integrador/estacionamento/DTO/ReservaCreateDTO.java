package projeto_integrador.estacionamento.DTO;

import java.time.LocalDateTime;

public record ReservaCreateDTO(
        Long vagaId,
        LocalDateTime inicio,
        LocalDateTime fim
) {
}
