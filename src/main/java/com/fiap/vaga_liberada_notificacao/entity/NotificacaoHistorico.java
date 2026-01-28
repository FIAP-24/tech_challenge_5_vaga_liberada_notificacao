package com.fiap.vaga_liberada_notificacao.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@Table(name = "historico_envios")
public class NotificacaoHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long pacienteId;
    private Long consultaId;

    @Column(columnDefinition = "TEXT")
    private String mensagem;

    private String canal; // WHATSAPP, EMAIL
    private LocalDateTime dataEnvio;
    private String status; // SUCESSO, FALHA


    public NotificacaoHistorico() {}

    public NotificacaoHistorico(Long pacienteId, Long consultaId, String mensagem, String canal, String status) {
        this.pacienteId = pacienteId;
        this.consultaId = consultaId;
        this.mensagem = mensagem;
        this.canal = canal;
        this.status = status;
        this.dataEnvio = LocalDateTime.now();
    }

    // Adicione os Getters se necessário para debug
}