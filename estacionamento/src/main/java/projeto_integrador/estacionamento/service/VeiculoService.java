package projeto_integrador.estacionamento.service;

import org.springframework.stereotype.Service;
import projeto_integrador.estacionamento.DTO.VeiculoCadastroDTO;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.entity.Veiculo;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import projeto_integrador.estacionamento.repository.VeiculoRepository;

import java.util.List;

@Service
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public VeiculoService(VeiculoRepository veiculoRepository,
                          UsuarioRepository usuarioRepository) {
        this.veiculoRepository = veiculoRepository;
        this.usuarioRepository = usuarioRepository;
    }

    public Veiculo cadastrarVeiculo(Long usuarioId, VeiculoCadastroDTO dto) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

        Veiculo veiculo = new Veiculo();
        veiculo.setCategoria(dto.getCategoria());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setMontadora(dto.getMontadora());
        veiculo.setModelo(dto.getModelo());
        veiculo.setUsuario(usuario);

        return veiculoRepository.save(veiculo);
    }

    public Veiculo editarVeiculo(Long veiculoId, Long usuarioId, VeiculoCadastroDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.getUsuario() == null || !veiculo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para editar este veículo");
        }

        veiculo.setCategoria(dto.getCategoria());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setMontadora(dto.getMontadora());
        veiculo.setModelo(dto.getModelo());

        return veiculoRepository.save(veiculo);
    }

    public void excluirVeiculo(Long veiculoId, Long usuarioId) {
        Veiculo veiculo = veiculoRepository.findById(veiculoId)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (veiculo.getUsuario() == null || !veiculo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Você não tem permissão para excluir este veículo");
        }

        veiculoRepository.delete(veiculo);
    }

    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }
}
