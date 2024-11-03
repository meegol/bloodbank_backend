package RedSource.Entities.DTO;

import RedSource.Entities.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import RedSource.Entities.enums.UserRoleType;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Integer userId;
    private Integer bloodTypeId;
    private UserRoleType role;
    private String name;
    private String email;
    private String profilePicture;
    private Date dateOfBirth;
    private String contactInformation;
    private Date createdAt;
    private Date updatedAt;

    /**
     * Constructs a UserDTO from a User entity.
     *
     * @param user the User entity
     */
    public UserDTO(User user) {
        this.userId = user.getUserId();
        this.bloodTypeId = user.getBloodTypeId();
        this.role = user.getRole();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profilePicture = user.getProfile_picture();
        this.dateOfBirth = user.getDateOfBirth();
        this.contactInformation = user.getContact_information();
        this.createdAt = user.getCreatedAt();
        this.updatedAt = user.getUpdatedAt();
    }
}
