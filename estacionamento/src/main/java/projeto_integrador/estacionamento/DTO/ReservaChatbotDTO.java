package projeto_integrador.estacionamento.DTO;

import java.time.LocalDateTime;

public record ReservaChatbotDTO(
        String telefone,
        LocalDateTime inicio,
        LocalDateTime fim,
        String categoriaVaga
) {
}
