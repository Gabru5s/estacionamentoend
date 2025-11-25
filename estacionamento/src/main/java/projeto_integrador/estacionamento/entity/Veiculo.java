package projeto_integrador.estacionamento.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "veiculos")
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_veiculo")
    private Long id;

    @Column(nullable = false, length = 20)
    private String categoria;

    @Column(nullable = false, length = 10)
    private String placa;

    @Column(nullable = false, length = 50)
    private String montadora;

    @Column(nullable = false, length = 50)
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "id_usuario")
    private Usuario usuario;

    public Veiculo() {
    }

    public Veiculo(Long id, String categoria, String placa, String montadora, String modelo, Usuario usuario) {
        this.id = id;
        this.categoria = categoria;
        this.placa = placa;
        this.montadora = montadora;
        this.modelo = modelo;
        this.usuario = usuario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getPlaca() {
        return placa;
    }

    public void setPlaca(String placa) {
        this.placa = placa;
    }

    public String getMontadora() {
        return montadora;
    }

    public void setMontadora(String montadora) {
        this.montadora = montadora;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }
}
