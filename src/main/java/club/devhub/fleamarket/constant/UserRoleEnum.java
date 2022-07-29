package club.devhub.fleamarket.constant;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;

import java.util.List;

/**
 * 用户具有的角色拥有的不同权限
 */
public enum UserRoleEnum {
    /**
     * 普通用户
     */
    ORDINARY_USER(0, AuthorityUtils.createAuthorityList("ROLE_USER")),
    /**
     * 管理员
     */
    ADMINISTRATOR(1, AuthorityUtils.createAuthorityList("ROLE_ADMIN"));
    private final int role;

    private final List<GrantedAuthority> authorities;

    UserRoleEnum(int role, List<GrantedAuthority> authorities) {
        this.role = role;
        this.authorities = authorities;
    }

    public int getRole() {
        return role;
    }

    public List<GrantedAuthority> getAuthorities() {
        return authorities;
    }


}
