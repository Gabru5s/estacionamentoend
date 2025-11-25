package projeto_integrador.estacionamento.entity;

import jakarta.persistence.*;
import projeto_integrador.estacionamento.enuns.VagaStatus;

@Entity
public class Vaga {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_vaga")
    private Long idVaga;

    @Column(nullable = false, length = 20)
    private String numero;

    @Column(nullable = false, length = 20)
    private String categoria;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private VagaStatus status;

    public Vaga() {
    }

    public Vaga(Long idVaga, String numero, String categoria, VagaStatus status) {
        this.idVaga = idVaga;
        this.numero = numero;
        this.categoria = categoria;
        this.status = status;
    }

    public Long getIdVaga() {
        return idVaga;
    }

    public void setIdVaga(Long idVaga) {
        this.idVaga = idVaga;
    }

    public String getNumero() {
        return numero;
    }

    public void setNumero(String numero) {
        this.numero = numero;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public VagaStatus getStatus() {
        return status;
    }

    public void setStatus(VagaStatus status) {
        this.status = status;
    }
}
