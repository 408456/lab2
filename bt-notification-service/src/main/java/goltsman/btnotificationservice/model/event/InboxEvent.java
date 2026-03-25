package goltsman.btnotificationservice.model.event;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "inbox_events")
public class InboxEvent {
    @Id
    private UUID id;
    private String aggregateType;
    private Long aggregateId;
    private String type;
    private String payload;
    private LocalDateTime receivedAt;
    private boolean processed;
}
