package projeto_integrador.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.enuns.VagaStatus;

import java.util.List;
import java.util.Optional;

@Repository
public interface VagaRepository extends JpaRepository<Vaga, Long> {

    // Exemplos de métodos úteis (opcionais, pode remover se não precisar agora):

    Optional<Vaga> findByNumero(String numero);

    List<Vaga> findByCategoria(String categoria);

    List<Vaga> findByStatus(VagaStatus status);
}
