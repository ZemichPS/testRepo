package by.zemich.userms.controller.request;

import lombok.Data;

@Data
public class ChangePasswordRequest {
    private String login;
    private String oldPassword;
    private String newPassword;
}
