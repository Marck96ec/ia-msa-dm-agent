package com.iaproject.agent.service.dto;

import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GiftReservationReportResponse {
    private Long eventId;
    private String eventSlug;
    private String eventName;
    private Integer totalRecords;
    private LocalDateTime generatedAt;
    private List<GiftReservationReportItem> items;
}
