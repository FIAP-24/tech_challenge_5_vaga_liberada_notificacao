package com.fiap.vaga_liberada_notificacao.service;

import com.fiap.vaga_liberada_notificacao.dto.NotificacaoSqsDto;
import com.fiap.vaga_liberada_notificacao.entity.NotificacaoHistorico;
import com.fiap.vaga_liberada_notificacao.repository.NotificacaoHistoricoRepository;
import org.springframework.stereotype.Service;

@Service
public class WhatsAppService {

    private final NotificacaoHistoricoRepository repository;

    public WhatsAppService(NotificacaoHistoricoRepository repository) {
        this.repository = repository;
    }

    public void enviarMensagem(NotificacaoSqsDto dto) {
        System.out.println("📱 [WhatsApp Provider] Iniciando conexão com a API...");

        try {
            // Simula latência de rede (1.5 segundos)
            Thread.sleep(1500);

            // Simula o envio
            System.out.println("📨 [WhatsApp Provider] Enviando para Paciente ID: " + dto.pacienteId());
            System.out.println("   --> Conteúdo: " + dto.mensagem());

            // Persiste o sucesso no banco
            NotificacaoHistorico historico = new NotificacaoHistorico(
                    dto.pacienteId(),
                    dto.consultaId(),
                    dto.mensagem(),
                    "WHATSAPP",
                    "ENVIADO_SUCESSO"
            );

            repository.save(historico);

            System.out.println("✅ [WhatsApp Provider] Mensagem entregue e registrada no banco (ID Log: " + historico.getId() + ")"); // Assumindo que você criou o getter getId()

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.err.println("❌ Erro no envio simulado");
        }
    }
}
