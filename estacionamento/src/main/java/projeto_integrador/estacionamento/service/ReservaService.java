package projeto_integrador.estacionamento.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import projeto_integrador.estacionamento.DTO.ReservaChatbotDTO;
import projeto_integrador.estacionamento.DTO.ReservaCreateDTO;
import projeto_integrador.estacionamento.DTO.ReservaRequestDTO;
import projeto_integrador.estacionamento.enuns.ReservaStatus;
import projeto_integrador.estacionamento.enuns.VagaStatus;
import projeto_integrador.estacionamento.entity.Horario;
import projeto_integrador.estacionamento.entity.Reserva;
import projeto_integrador.estacionamento.entity.Usuario;
import projeto_integrador.estacionamento.entity.Vaga;
import projeto_integrador.estacionamento.excecoesPersonalisadas.ConflictException;
import projeto_integrador.estacionamento.excecoesPersonalisadas.NotFoundException;
import projeto_integrador.estacionamento.repository.HorarioRepository;
import projeto_integrador.estacionamento.repository.ReservaRepository;
import projeto_integrador.estacionamento.repository.UsuarioRepository;
import projeto_integrador.estacionamento.repository.VagaRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservaService {

    private final ReservaRepository reservaRepository;
    private final UsuarioRepository usuarioRepository;
    private final VagaRepository vagaRepository;
    private final HorarioRepository horarioRepository;

    @Transactional
    public Reserva criar(ReservaRequestDTO req) {

        if (req.inicio() == null || req.fim() == null) {
            throw new ConflictException("Início e fim são obrigatórios");
        }
        if (!req.fim().isAfter(req.inicio())) {
            throw new ConflictException("Fim deve ser após o início");
        }

        Usuario usuario = usuarioRepository.findById(req.usuarioId())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Vaga vaga = vagaRepository.findById(req.vagaId())
                .orElseThrow(() -> new NotFoundException("Vaga não encontrada"));

        var bloqueantes = List.of(ReservaStatus.PENDENTE, ReservaStatus.CONFIRMADO);
        boolean conflito = reservaRepository.existeConflito(
                vaga.getIdVaga(), req.inicio(), req.fim(), bloqueantes);

        if (conflito) {
            throw new ConflictException("Vaga indisponível no intervalo informado");
        }

        if (vaga.getStatus() == VagaStatus.OCUPADA) {
            throw new ConflictException("Vaga ocupada por ticket em aberto");
        }

        Horario horario = horarioRepository
                .findByInicioAndFim(req.inicio(), req.fim())
                .orElseGet(() -> {
                    Horario h = new Horario();
                    h.setInicio(req.inicio());
                    h.setFim(req.fim());
                    return horarioRepository.save(h);
                });

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setVaga(vaga);
        reserva.setHorario(horario);
        reserva.setStatus(ReservaStatus.CONFIRMADO);

        if (reserva.getStatus() == ReservaStatus.CONFIRMADO) {
            vaga.setStatus(VagaStatus.RESERVADA);
        }

        return reservaRepository.save(reserva);
    }

    //MÉTODO para usuário autenticado (usado pelo chatbot)

    @Transactional
    public Reserva criarParaUsuario(Long usuarioId, ReservaCreateDTO req) {

        if (req.inicio() == null || req.fim() == null) {
            throw new ConflictException("Início e fim são obrigatórios");
        }

        if (!req.fim().isAfter(req.inicio())) {
            throw new ConflictException("Fim deve ser após o início");
        }

        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        Vaga vaga = vagaRepository.findById(req.vagaId())
                .orElseThrow(() -> new NotFoundException("Vaga não encontrada"));

        var bloqueantes = List.of(ReservaStatus.PENDENTE, ReservaStatus.CONFIRMADO);

        boolean conflito = reservaRepository.existeConflito(
                vaga.getIdVaga(),
                req.inicio(),
                req.fim(),
                bloqueantes
        );

        if (conflito) {
            throw new ConflictException("Vaga indisponível no intervalo informado");
        }

        if (vaga.getStatus() == VagaStatus.OCUPADA) {
            throw new ConflictException("Vaga ocupada por ticket em aberto");
        }

        Horario horario = horarioRepository
                .findByInicioAndFim(req.inicio(), req.fim())
                .orElseGet(() -> {
                    Horario h = new Horario();
                    h.setInicio(req.inicio());
                    h.setFim(req.fim());
                    return horarioRepository.save(h);
                });

        Reserva reserva = new Reserva();
        reserva.setUsuario(usuario);
        reserva.setVaga(vaga);
        reserva.setHorario(horario);
        reserva.setStatus(ReservaStatus.CONFIRMADO);

        vaga.setStatus(VagaStatus.RESERVADA);

        return reservaRepository.save(reserva);
    }


    //Listagem
    @Transactional(readOnly = true)
    public List<Reserva> listarPorUsuario(Long usuarioId) {
        return reservaRepository.findByUsuarioId(usuarioId);
    }

    @Transactional(readOnly = true)
    public List<Reserva> listarTodas() {
        return reservaRepository.findAll();
    }

    @Transactional
    public Reserva criarViaChatbot(ReservaChatbotDTO dto) {

        if (dto.inicio() == null || dto.fim() == null) {
            throw new ConflictException("Início e fim são obrigatórios");
        }

        if (!dto.fim().isAfter(dto.inicio())) {
            throw new ConflictException("Fim deve ser após o início");
        }

        //Buscar usuário pelo telefone
        Usuario usuario = usuarioRepository.findByTelefone(dto.telefone())
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

        //Buscar qualquer vaga LIVRE
        Vaga vagaLivre = vagaRepository.findByStatus(VagaStatus.LIVRE).stream()
                .findFirst()
                .orElseThrow(() -> new ConflictException("Não há vagas livres nesse período"));

        ReservaCreateDTO createDTO = new ReservaCreateDTO(
                vagaLivre.getIdVaga(),
                dto.inicio(),
                dto.fim()
        );
        return criarParaUsuario(usuario.getId(), createDTO);
    }

}
