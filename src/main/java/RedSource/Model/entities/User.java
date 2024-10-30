package RedSource.Model.entities;

import RedSource.Model.enums.UserRoleType;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.io.Serial;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "Users")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class User implements Serializable {

    @Serial
    private static final long serialVersionUID = 5364496601245150928L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_id")
    Integer userId;

    @Column(name = "blood_type_id")
    @JsonProperty("blood_type_id")
    Integer bloodTypeId;

    @Enumerated(EnumType.STRING)
    @Column(name = "roles", length = 50)
    UserRoleType roles;

    @Column(name = "name", length = 100)
    String name;

    @Column(name = "email", length = 100, unique = true)
    String email;

    @Column(name = "profile_picture", length = 100)
    String profile_picture;

    @Column(name = "username", length = 100, unique = true)
    String username;

    @Column(name = "password", length = 100)
    String password;

    @Temporal(TemporalType.DATE)
    @Column(name = "date_of_birth")
    @JsonProperty("date_of_birth")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    Date dateOfBirth;

    @JsonProperty("contact_information")
    @Column(name = "contact_information", length = 255)
    String contact_information;

    @Column(name = "created_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date createdAt;

    @Column(name = "updated_at")
    @Temporal(TemporalType.TIMESTAMP)
    Date updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = new Date();
        updatedAt = new Date();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}
