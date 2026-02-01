package com.fiap.vaga_liberada_notificacao.infrastructure.listener;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fiap.vaga_liberada_notificacao.dto.NotificacaoSqsDto;
import com.fiap.vaga_liberada_notificacao.service.WhatsAppService;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

class NotificacaoListenerTest {

    @Test
    void receberNotificacao_validJson_callsService() throws Exception {
        // Arrange
        WhatsAppService whatsAppService = mock(WhatsAppService.class);
        ObjectMapper mapper = new ObjectMapper();

        NotificacaoListener listener = new NotificacaoListener(whatsAppService, mapper);

        NotificacaoSqsDto dto = new NotificacaoSqsDto(10L, 20L, "Mensagem teste");
        String json = mapper.writeValueAsString(dto);

        // Act
        listener.receberNotificacao(json);

        // Assert
        verify(whatsAppService, times(1)).enviarMensagem(dto);
    }

    @Test
    void receberNotificacao_invalidJson_doesNotCallService() {
        // Arrange
        WhatsAppService whatsAppService = mock(WhatsAppService.class);
        ObjectMapper mapper = new ObjectMapper();

        NotificacaoListener listener = new NotificacaoListener(whatsAppService, mapper);

        String badJson = "{ this is not valid json }";

        // Act
        listener.receberNotificacao(badJson);

        // Assert: service must not be called and no exception thrown
        verifyNoInteractions(whatsAppService);
    }
}
