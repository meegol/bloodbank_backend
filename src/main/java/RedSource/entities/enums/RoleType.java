package RedSource.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import static RedSource.entities.enums.PermissionType.*;

@Getter
@RequiredArgsConstructor
public enum RoleType {
    DONOR(
        Set.of(
            DONOR_READ,
            DONOR_UPDATE
        )
    ),
    BLOODBANK(
        Set.of(
            BLOODBANK_READ,
            BLOODBANK_WRITE,
            BLOODBANK_UPDATE,
            BLOODBANK_DELETE
        )
    ),
    HOSPITAL(
        Set.of(
            HOSPITAL_READ,
            HOSPITAL_WRITE,
            HOSPITAL_UPDATE
        )
    ),
    ADMIN(
        Set.of(
            ADMIN_READ,
            ADMIN_WRITE,
            ADMIN_UPDATE,
            ADMIN_DELETE
        )
    ),
    USER(Collections.emptySet());

    private final Set<PermissionType> permissions;

    public List<SimpleGrantedAuthority> getAuthorities() {
        var authorities = new java.util.ArrayList<>(
            getPermissions()
                .stream()
                .map(permission -> new SimpleGrantedAuthority(permission.getPermission()))
                .toList()
        );
        authorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return authorities;
    }
}
