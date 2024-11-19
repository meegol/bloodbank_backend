package RedSource.dto;

import RedSource.entities.enums.RoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.Date;

@Builder
public record RegisterRequest(
    @NotBlank(message = "Name is required")
    String name,

    @NotBlank(message = "Email is required")
    @Email(message = "Invalid email format")
    String email,

    @NotBlank(message = "Password is required")
    String password,

    @NotNull(message = "Date of birth is required")
    Date dateOfBirth,

    @NotBlank(message = "Contact information is required")
    String contactInformation,

    @NotNull(message = "Role is required")
    RoleType roles
) {}
