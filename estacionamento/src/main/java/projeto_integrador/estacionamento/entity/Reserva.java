package projeto_integrador.estacionamento.entity;

import jakarta.persistence.*;
import projeto_integrador.estacionamento.enuns.ReservaStatus;

@Entity
@Table(name = "reservas")
public class Reserva {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_reserva")
    private Long idReserva;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_vaga", nullable = false)
    private Vaga vaga;

    @ManyToOne(optional = false)
    @JoinColumn(name = "id_horario", nullable = false)
    private Horario horario;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 20)
    private ReservaStatus status;

    public Reserva() {
    }

    public Reserva(Long idReserva, Usuario usuario, Vaga vaga, Horario horario, ReservaStatus status) {
        this.idReserva = idReserva;
        this.usuario = usuario;
        this.vaga = vaga;
        this.horario = horario;
        this.status = status;
    }

    public Long getIdReserva() {
        return idReserva;
    }

    public void setIdReserva(Long idReserva) {
        this.idReserva = idReserva;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Vaga getVaga() {
        return vaga;
    }

    public void setVaga(Vaga vaga) {
        this.vaga = vaga;
    }

    public Horario getHorario() {
        return horario;
    }

    public void setHorario(Horario horario) {
        this.horario = horario;
    }

    public ReservaStatus getStatus() {
        return status;
    }

    public void setStatus(ReservaStatus status) {
        this.status = status;
    }
}
