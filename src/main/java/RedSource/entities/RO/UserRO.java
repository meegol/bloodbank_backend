package RedSource.entities.RO;

import RedSource.entities.User;
import RedSource.entities.enums.UserRoleType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRO {

    private Integer userId;

    @NotBlank(message = "Name is mandatory.")
    private String name;

    @NotBlank(message = "Email is mandatory.")
    @Email(message = "Invalid email format.")
    private String email;

    @NotBlank(message = "Username is mandatory.")
    private String username;

    private String password;

    @NotBlank(message = "Contact information is mandatory.")
    private String contactInformation;

    @NotNull(message = "Role is mandatory.")
    private UserRoleType role;

    @NotNull(message = "Blood Type ID is mandatory.")
    private Integer bloodTypeId;
    private String profilePicture;
    private Date dateOfBirth;


    /**
     * Converts this RO to a User entity.
     *
     * @param user the User entity to populate
     * @return the populated User entity
     */
    public User toEntity(User user) {
        if (user == null) {
            user = new User();
        }

        user.setName(this.name);
        user.setEmail(this.email);
        user.setContact_information(this.contactInformation);
        user.setRole(this.role);
        user.setBloodTypeId(this.bloodTypeId);
        user.setProfile_picture(this.profilePicture);
        user.setDateOfBirth(this.dateOfBirth);

        if (this.password != null && !this.password.isBlank()) {
            user.setPassword(this.password);
        }

        return user;
    }
}