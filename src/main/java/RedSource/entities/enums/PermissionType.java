package RedSource.entities.enums;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PermissionType {
    // Donor permissions
    DONOR_READ("donor:read"),
    DONOR_UPDATE("donor:update"),

    // Blood Bank permissions
    BLOODBANK_READ("bloodbank:read"),
    BLOODBANK_WRITE("bloodbank:write"),
    BLOODBANK_UPDATE("bloodbank:update"),
    BLOODBANK_DELETE("bloodbank:delete"),

    // Hospital permissions
    HOSPITAL_READ("hospital:read"),
    HOSPITAL_WRITE("hospital:write"),
    HOSPITAL_UPDATE("hospital:update"),

    // Admin permissions
    ADMIN_READ("admin:read"),
    ADMIN_WRITE("admin:write"),
    ADMIN_UPDATE("admin:update"),
    ADMIN_DELETE("admin:delete");

    @Getter
    private final String permission;
}
