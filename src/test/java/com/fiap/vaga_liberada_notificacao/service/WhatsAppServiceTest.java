package com.fiap.vaga_liberada_notificacao.service;

import com.fiap.vaga_liberada_notificacao.dto.NotificacaoSqsDto;
import com.fiap.vaga_liberada_notificacao.entity.NotificacaoHistorico;
import com.fiap.vaga_liberada_notificacao.repository.NotificacaoHistoricoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class WhatsAppServiceTest {

    @Test
    void testEnviarMensagem_Success() {
        // Arrange
        NotificacaoHistoricoRepository repo = mock(NotificacaoHistoricoRepository.class);

        // Subclasse de teste que elimina a espera
        WhatsAppService service = new WhatsAppService(repo) {
            @Override
            protected void sleepMillis(long millis) throws InterruptedException {

            }
        };

        NotificacaoSqsDto dto = new NotificacaoSqsDto(123L, 456L, "Consulta liberada");

        ArgumentCaptor<NotificacaoHistorico> captor = ArgumentCaptor.forClass(NotificacaoHistorico.class);

        // Act
        service.enviarMensagem(dto);

        // Assert
        verify(repo, times(1)).save(captor.capture());
        NotificacaoHistorico saved = captor.getValue();
        assertEquals(dto.pacienteId(), saved.getPacienteId());
        assertEquals(dto.consultaId(), saved.getConsultaId());
        assertEquals(dto.mensagem(), saved.getMensagem());
        assertEquals("WHATSAPP", saved.getCanal());
        assertEquals("ENVIADO_SUCESSO", saved.getStatus());
        assertNotNull(saved.getDataEnvio());
    }

    @Test
    void testEnviarMensagem_Interrupted() {
        // Arrange
        NotificacaoHistoricoRepository repo = mock(NotificacaoHistoricoRepository.class);

        // Subclasse que simula InterruptedException
        WhatsAppService service = new WhatsAppService(repo) {
            @Override
            protected void sleepMillis(long millis) throws InterruptedException {
                throw new InterruptedException("simulated");
            }
        };

        NotificacaoSqsDto dto = new NotificacaoSqsDto(1L, 2L, "Mensagem");

        // Act
        service.enviarMensagem(dto);

        // Assert: save should not be called
        verify(repo, times(0)).save(any());

        // Also check that the thread interrupted flag is set (method sets it)
        assertTrue(Thread.currentThread().isInterrupted() || Thread.interrupted());
    }
}
