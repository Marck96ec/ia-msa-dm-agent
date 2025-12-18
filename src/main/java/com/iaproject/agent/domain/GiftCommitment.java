package com.iaproject.agent.domain;

import com.iaproject.agent.domain.enums.CommitmentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

/**
 * Entidad que representa el compromiso de un invitado con un regalo.
 * Puede ser una reserva completa o un aporte parcial (regalo compartido).
 * 
 * Cada compromiso genera un token único que permite al invitado consultar
 * o cancelar su reserva/aporte sin necesidad de login.
 */
@Entity
@Table(name = "gift_commitments", indexes = {
    @Index(name = "idx_commitment_gift", columnList = "gift_id"),
    @Index(name = "idx_commitment_user", columnList = "user_id"),
    @Index(name = "idx_commitment_token", columnList = "token", unique = true),
    @Index(name = "idx_commitment_active", columnList = "isActive")
})
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftCommitment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /**
     * Referencia al regalo reservado/aportado.
     */
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "gift_id", nullable = false, foreignKey = @ForeignKey(name = "fk_commitment_gift"))
    private Gift gift;

    /**
     * Identificador del usuario que realizó el compromiso.
     */
    @Column(name = "user_id", nullable = false, length = 100)
    private String userId;

    /**
     * Nombre del invitado (capturado del chat).
     */
    @Column(name = "guest_name", length = 200)
    private String guestName;

    /**
     * Email del invitado (opcional).
     */
    @Column(name = "guest_email", length = 150)
    private String guestEmail;

    /**
     * Teléfono del invitado (opcional).
     */
    @Column(name = "guest_phone", length = 50)
    private String guestPhone;

    /**
     * Tipo de compromiso: FULL_RESERVATION o PARTIAL_CONTRIBUTION.
     */
    @Enumerated(EnumType.STRING)
    @Column(name = "commitment_type", nullable = false, length = 30)
    private CommitmentType commitmentType;

    /**
     * Monto aportado (solo para contribuciones parciales).
     * Para reservas completas, puede ser null o igual al precio del regalo.
     */
    @Column(name = "contribution_amount", precision = 10, scale = 2)
    private BigDecimal contributionAmount;

    /**
     * Token único generado para que el invitado consulte/cancele su compromiso.
     * Formato: UUID o hash seguro.
     */
    @Column(name = "token", nullable = false, unique = true, length = 100)
    private String token;

    /**
     * Indica si el compromiso está activo (no cancelado).
     */
    @Column(name = "is_active", nullable = false)
    @Builder.Default
    private Boolean isActive = true;

    /**
     * Notas del invitado (ej: "Lo compraré en Amazon").
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * Fecha y hora en que se canceló el compromiso (si aplica).
     */
    @Column(name = "cancelled_at")
    private OffsetDateTime cancelledAt;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false, updatable = false)
    private OffsetDateTime createdAt;
}
