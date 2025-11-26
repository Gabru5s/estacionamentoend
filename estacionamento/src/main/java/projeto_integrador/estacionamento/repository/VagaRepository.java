package projeto_integrador.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.enuns.VagaStatus;

import java.util.List;
import java.util.Optional;

public interface VagaRepository extends JpaRepository<Vaga, Long> {

    List<Vaga> findByStatus(VagaStatus status);

    List<Vaga> findByCategoriaAndStatusOrderByIdVagaAsc(
            String categoria,
            VagaStatus status
    );

    Optional<Vaga> findFirstByCategoriaAndStatusOrderByIdVagaAsc(
            String categoria,
            VagaStatus status
    );
}
