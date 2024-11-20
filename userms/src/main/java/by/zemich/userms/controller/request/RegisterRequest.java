package by.zemich.userms.controller.request;

import by.zemich.userms.dao.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RegisterRequest {
    @Pattern(regexp = "^\\s*\\S.*$", message = "username must not empty")
    private String username;
    @Email(message = "invalid email")
    private String email;
    @Size(min = 6, message = "Too weak password. Min password length 6 characters")
    @Pattern(regexp = "^\\s*\\S.*$", message = "password name must not empty")
    private String password;
    @Pattern(regexp = "^\\s*\\S.*$", message = "first name must not empty")
    private String firstName;
    @Pattern(regexp = "^\\s*\\S.*$", message = "last name must not empty")
    private String lastName;
    @Pattern(regexp = "^\\s*\\S.*$", message = "role name must not empty")
    private String role;
}
