package projeto_integrador.estacionamento.repository;

import projeto_integrador.estacionamento.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface    UsuarioRepository extends JpaRepository<Usuario, Long> {

    // Métodos para verificar se email ou CPF já estão em uso
    Optional<Usuario> findByEmail(String email);
    Optional<Usuario> findByCpf(String cpf);
}