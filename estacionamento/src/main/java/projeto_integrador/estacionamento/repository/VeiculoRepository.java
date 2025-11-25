package projeto_integrador.estacionamento.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import projeto_integrador.estacionamento.entity.Veiculo;

import java.util.List;
import java.util.Optional;

public interface VeiculoRepository extends JpaRepository<Veiculo, Long> {
    List<Veiculo> findByUsuarioId(Long usuarioId);
    Optional<Veiculo> findByPlaca(String placa);
    boolean existsByPlaca(String placa);
}
