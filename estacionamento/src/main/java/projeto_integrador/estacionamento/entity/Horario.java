package projeto_integrador.estacionamento.entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(
        name = "horario",
        uniqueConstraints = @UniqueConstraint(
                name = "uk_horario_inicio_fim",
                columnNames = {"inicio", "fim"}
        )
)
public class Horario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_horario")
    private Long idHorario;

    @Column(name = "inicio", nullable = false)
    private LocalDateTime inicio;

    @Column(name = "fim", nullable = false)
    private LocalDateTime fim;

    public Horario() {
    }

    public Horario(Long idHorario, LocalDateTime inicio, LocalDateTime fim) {
        this.idHorario = idHorario;
        this.inicio = inicio;
        this.fim = fim;
    }

    public Long getIdHorario() {
        return idHorario;
    }

    public void setIdHorario(Long idHorario) {
        this.idHorario = idHorario;
    }

    public LocalDateTime getInicio() {
        return inicio;
    }

    public void setInicio(LocalDateTime inicio) {
        this.inicio = inicio;
    }

    public LocalDateTime getFim() {
        return fim;
    }

    public void setFim(LocalDateTime fim) {
        this.fim = fim;
    }
}
