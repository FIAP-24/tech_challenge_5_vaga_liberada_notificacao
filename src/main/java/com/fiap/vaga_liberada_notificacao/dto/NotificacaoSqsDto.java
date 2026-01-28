package com.fiap.vaga_liberada_notificacao.dto;

public record NotificacaoSqsDto(
        Long pacienteId,
        Long consultaId,
        String mensagem
) {}