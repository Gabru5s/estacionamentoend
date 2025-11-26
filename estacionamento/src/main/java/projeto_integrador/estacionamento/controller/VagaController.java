package projeto_integrador.estacionamento.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import projeto_integrador.estacionamento.enuns.VagaStatus;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.repository.VagaRepository;

import java.util.List;

@RestController
@RequestMapping("/api/vagas")
@RequiredArgsConstructor
public class VagaController {

    private final VagaRepository vagaRepository;

    /**
     * Lista todas as vagas (ocupadas, reservadas, livres etc.)
     */
    @GetMapping
    public ResponseEntity<List<Vaga>> listarTodas() {
        List<Vaga> vagas = vagaRepository.findAll();
        return ResponseEntity.ok(vagas);
    }

    /**
     * Lista vagas LIVRES.
     *
     * - /api/vagas/livres              -> todas as livres
     * - /api/vagas/livres?categoria=CARRO -> livres só de CARRO
     * - /api/vagas/livres?categoria=MOTO  -> livres só de MOTO
     * - /api/vagas/livres?categoria=PCD   -> livres só de PCD
     */
    @GetMapping("/livres")
    public ResponseEntity<List<Vaga>> listarLivres(
            @RequestParam(required = false) String categoria
    ) {
        List<Vaga> vagas;

        if (categoria == null || categoria.isBlank()) {
            // todas as vagas livres
            vagas = vagaRepository.findByStatus(VagaStatus.LIVRE);
        } else {
            String catNormalizada = categoria.trim().toUpperCase();
            vagas = vagaRepository.findByCategoriaAndStatusOrderByIdVagaAsc(
                    catNormalizada,
                    VagaStatus.LIVRE
            );
        }

        return ResponseEntity.ok(vagas);
    }

    /**
     * (Opcional) Detalhe de uma vaga específica, se você quiser usar no front.
     */
    @GetMapping("/{id}")
    public ResponseEntity<Vaga> buscarPorId(@PathVariable Long id) {
        return vagaRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
