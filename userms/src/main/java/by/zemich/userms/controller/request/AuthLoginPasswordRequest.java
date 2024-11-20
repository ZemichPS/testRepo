package by.zemich.userms.controller.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class AuthLoginPasswordRequest {
    private String login;
    private String password;
}
