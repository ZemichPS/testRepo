package by.zemich.userms.controller.response;

import by.zemich.userms.dao.entity.Role;
import jakarta.persistence.Id;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
@Builder
public class UserFullResponse {
    private UUID id;
    private String username;
    private String firstName;
    private String lastName;
    @CreationTimestamp(source = SourceType.DB)
    private LocalDateTime registerAt;
    private boolean active;
    @UpdateTimestamp(source = SourceType.DB)
    private LocalDateTime updatedAt;
    private String role;
}
