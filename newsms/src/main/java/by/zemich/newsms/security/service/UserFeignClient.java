package by.zemich.newsms.security.service;

import by.zemich.newsms.core.domain.UserDTO;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.SourceType;
import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.time.LocalDateTime;
import java.util.UUID;

@FeignClient(name = "user-ms")
public interface UserFeignClient {

    @RequestMapping(
            method = RequestMethod.GET,
            path = "/api/v1/users/{userId}"
    )
    UserFullResponse getUserByUsername(@PathVariable("userId") String userId,
                              @RequestHeader("Authorization") String tokenBearer
    );

    @Data
    public static class UserFullResponse {
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

}
