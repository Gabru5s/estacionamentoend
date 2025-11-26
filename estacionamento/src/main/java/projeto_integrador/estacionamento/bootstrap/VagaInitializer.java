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
            return; // já existem vagas
        }

        int numero = 1;

        // 30 vagas de CARRO
        for (; numero <= 30; numero++) {
            String codigo = String.format("V%02d", numero);
            Vaga vaga = Vaga.builder()
                    .numero(codigo)
                    .categoria("CARRO")
                    .status(VagaStatus.LIVRE)
                    .build();
            vagaRepository.save(vaga);
        }

        // 15 vagas de MOTO (31 a 45)
        for (; numero <= 45; numero++) {
            String codigo = String.format("V%02d", numero);
            Vaga vaga = Vaga.builder()
                    .numero(codigo)
                    .categoria("MOTO")
                    .status(VagaStatus.LIVRE)
                    .build();
            vagaRepository.save(vaga);
        }

        // 5 vagas PCD (46 a 50)
        for (; numero <= 50; numero++) {
            String codigo = String.format("V%02d", numero);
            Vaga vaga = Vaga.builder()
                    .numero(codigo)
                    .categoria("PCD")
                    .status(VagaStatus.LIVRE)
                    .build();
            vagaRepository.save(vaga);
        }

        System.out.println("==== Vagas padrão criadas (30 CARRO, 15 MOTO, 5 PCD) ====");
    }
}
