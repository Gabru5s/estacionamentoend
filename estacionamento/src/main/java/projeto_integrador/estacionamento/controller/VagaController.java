package projeto_integrador.estacionamento.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.enuns.VagaStatus;
import projeto_integrador.estacionamento.repository.VagaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
public class VagaController {

    private final VagaRepository vagaRepository;

    public VagaController(VagaRepository vagaRepository) {
        this.vagaRepository = vagaRepository;
    }

    @GetMapping("/livres")
    public List<Vaga> listarVagasLivres() {
        return VagaRepository.findByStatus(VagaStatus.LIVRE);
    }
}

