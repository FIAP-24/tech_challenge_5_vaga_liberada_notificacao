package com.fiap.vaga_liberada_notificacao.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.vaga_liberada_notificacao.dto.NotificacaoSqsDto;
import com.fiap.vaga_liberada_notificacao.service.WhatsAppService;
import io.awspring.cloud.sqs.annotation.SqsListener;
import org.springframework.stereotype.Component;

@Component
public class NotificacaoListener {

    private final WhatsAppService whatsAppService;
    private final ObjectMapper objectMapper;

    public NotificacaoListener(WhatsAppService whatsAppService, ObjectMapper objectMapper) {
        this.whatsAppService = whatsAppService;
        this.objectMapper = objectMapper;
    }

    @SqsListener("notificacao-agenda-queue")
    public void receberNotificacao(String mensagemJson) {
        try {
            // Converte o JSON String para Objeto Java
            NotificacaoSqsDto dto = objectMapper.readValue(mensagemJson, NotificacaoSqsDto.class);

            // Chama o serviço de simulação
            whatsAppService.enviarMensagem(dto);

        } catch (Exception e) {
            System.err.println("Erro ao processar mensagem da fila: " + e.getMessage());
            // Em produção, aqui lançaríamos exceção para a mensagem ir para uma DLQ (Dead Letter Queue)
        }
    }
}