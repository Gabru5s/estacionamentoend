package projeto_integrador.estacionamento.bootstrap;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.enuns.VagaStatus;
import projeto_integrador.estacionamento.repository.VagaRepository;

@Component
@RequiredArgsConstructor
public class VagaInitializer {

    private final VagaRepository vagaRepository;

    @PostConstruct
    public void criarVagasPadroes() {
        if (vagaRepository.count() > 0) {
            return; // Já existem vagas
        }

        for (int i = 1; i <= 64; i++) {
            String numero = String.format("V%02d", i);

            Vaga vaga = Vaga.builder()
                    .numero(numero)
                    .categoria("CARRO") // ou PADRAO
                    .status(VagaStatus.LIVRE)
                    .build();

            vagaRepository.save(vaga);
        }

        System.out.println("==== 64 vagas criadas automaticamente ====");
    }
}
