package goltsman.btnotificationservice.model.event;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "notifications")
public class Notification {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long userId;
    private String type;
    private String title;
    private String message;
    private boolean emailSent;
    private boolean smsSent;
    private boolean read;
    private LocalDateTime createdAt;
}
