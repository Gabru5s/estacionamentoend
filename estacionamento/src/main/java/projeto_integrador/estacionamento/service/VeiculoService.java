package projeto_integrador.estacionamento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import projeto_integrador.estacionamento.DTO.VeiculoCadastroDTO;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.entity.Veiculo;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import projeto_integrador.estacionamento.repository.VeiculoRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VeiculoService {

    private final VeiculoRepository veiculoRepository;
    private final UsuarioRepository usuarioRepository;

    public Veiculo cadastrarVeiculo(Long usuarioId, VeiculoCadastroDTO dto) {

        if (veiculoRepository.findByPlaca(dto.getPlaca()).isPresent()) {
            throw new RuntimeException("Placa já cadastrada.");
        }

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

    public Veiculo editarVeiculo(Long id, Long usuarioId, VeiculoCadastroDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (!veiculo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado");
        }

        veiculoRepository.findByPlaca(dto.getPlaca()).ifPresent(v -> {
            if (!v.getId().equals(id)) {
                throw new RuntimeException("Placa já cadastrada.");
            }
        });

        veiculo.setCategoria(dto.getCategoria());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setMontadora(dto.getMontadora());
        veiculo.setModelo(dto.getModelo());

        return veiculoRepository.save(veiculo);
    }


    public void excluirVeiculo(Long id, Long usuarioId) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        if (!veiculo.getUsuario().getId().equals(usuarioId)) {
            throw new RuntimeException("Usuário não autorizado");
        }

        veiculoRepository.delete(veiculo);
    }
    public List<Veiculo> listarTodos() {
        return veiculoRepository.findAll();
    }

    public Veiculo editarVeiculoAdmin(Long id, VeiculoCadastroDTO dto) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        veiculoRepository.findByPlaca(dto.getPlaca()).ifPresent(v -> {
            if (!v.getId().equals(id)) {
                throw new RuntimeException("Placa já cadastrada.");
            }
        });

        veiculo.setCategoria(dto.getCategoria());
        veiculo.setPlaca(dto.getPlaca());
        veiculo.setMontadora(dto.getMontadora());
        veiculo.setModelo(dto.getModelo());

        return veiculoRepository.save(veiculo);
    }

    public void excluirVeiculoAdmin(Long id) {
        Veiculo veiculo = veiculoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Veículo não encontrado"));

        veiculoRepository.delete(veiculo);
    }
}
