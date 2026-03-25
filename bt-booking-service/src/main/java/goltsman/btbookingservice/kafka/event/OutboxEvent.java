package goltsman.btbookingservice.kafka.event;

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
@Table(name = "outbox_event")
public class OutboxEvent {

    @Id
    private UUID id;

    private String aggregateType;
    private Long aggregateId;
    private String type;

    @Column(columnDefinition = "text")
    private String payload;

    private LocalDateTime createdAt;
    private boolean processed;
}
