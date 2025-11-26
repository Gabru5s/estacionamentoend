package projeto_integrador.estacionamento.service;

import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.enuns.VagaStatus;
import projeto_integrador.estacionamento.repository.VagaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VagaService {

    private final VagaRepository vagaRepository;

    public List<Vaga> listarVagasLivres(){
        return vagaRepository.findByStatus(VagaStatus.LIVRE);
    }

    public Vaga reservarVaga(Vaga vaga) {
        vaga.setStatus(VagaStatus.RESERVADA);
        return vagaRepository.save(vaga);
    }

    public void liberarVaga(Vaga vaga) {
        vaga.setStatus(VagaStatus.LIVRE);
        vagaRepository.save(vaga);
    }
}
