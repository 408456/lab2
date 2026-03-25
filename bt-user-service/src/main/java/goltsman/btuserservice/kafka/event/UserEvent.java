package goltsman.btuserservice.kafka.event;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserEvent {
    private UUID eventId;
    private UserEventType type;
    private Long userId;
    private String email;
    private String phone;
    private String firstName;
    private String lastName;
}
