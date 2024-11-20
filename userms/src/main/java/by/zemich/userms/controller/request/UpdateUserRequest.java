package by.zemich.userms.controller.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {
    @Pattern(regexp = "^\\s*\\S.*$", message = "username must not empty")
    private String username;
    @Email(message = "invalid email")
    private String email;
    @Pattern(regexp = "^\\s*\\S.*$", message = "first name must not empty")
    private String firstName;
    @Pattern(regexp = "^\\s*\\S.*$", message = "last name must not empty")
    private String lastName;
}
