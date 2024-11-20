package by.zemich.newsms.core.domain;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.UUID;

@Data
public class UserDTO {
    private UUID id;
    private String username;
    private Role role;
}

enum Role {
    ADMIN, JOURNALIST, SUBSCRIBER;
}

