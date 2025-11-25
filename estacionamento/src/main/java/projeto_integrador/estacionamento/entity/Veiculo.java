package projeto_integrador.estacionamento.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Veiculo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String categoria;

    @Column(unique = true)
    private String placa;

    private String montadora;
    private String modelo;

    @ManyToOne
    @JoinColumn(name = "usuario_id")
    @JsonBackReference // ✅ Impede que o Usuario seja serializado de volta
    private Usuario usuario;
}