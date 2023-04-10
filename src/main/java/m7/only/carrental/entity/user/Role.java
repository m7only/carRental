package m7.only.carrental.entity.user;

import org.springframework.security.core.GrantedAuthority;

/**
 * Enum ролей пользователей.
 */
public enum Role implements GrantedAuthority {
    ROLE_CLIENT,
    ROLE_APPROVED_CLIENT,
    ROLE_MANAGER,
    ROLE_ADMIN;

    @Override
    public String getAuthority() {
        return name();
    }

}
