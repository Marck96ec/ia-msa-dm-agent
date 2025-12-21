package com.iaproject.agent.service.dto;

import com.iaproject.agent.domain.enums.CommitmentType;
import com.iaproject.agent.domain.enums.GiftStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftReservationReportItem {
    private Long giftId;
    private String giftName;
    private GiftStatus giftStatus;
    private CommitmentType commitmentType;
    private String reserverUserId;
    private String reserverName;
    private String reserverEmail;
    private String reserverPhone;
    private BigDecimal contributionAmount;
    private String notes;
    private LocalDateTime reservedAt;
}
