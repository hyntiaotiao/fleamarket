package club.devhub.fleamarket.entity;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

import club.devhub.fleamarket.constant.UserRoleEnum;
import lombok.Data;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.util.Assert;

/**
 * 
 * @TableName t_user
 * Serializable接口用于序列化
 */
@Data
public class User implements Serializable,UserDetails {
    /**
     * 用户ID
     */
    private Long userId;

    /**
     * 用户名
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 地址
     */
    private String address;

    /**
     * 性别
     */
    private Integer sex;

    /**
     * 角色
     */
    private Integer role;

    /**
     * 用户状态
     * */
    private Integer userStatus;

    /**
     * 更新时间
     */

    private LocalDateTime updateTime;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    private static final long serialVersionUID = 1L;

    /**
     * 根据用户角色获得其权限并返回
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        Assert.notNull(this.role, "role不能为null");
        for (UserRoleEnum roleEnum : UserRoleEnum.values()) {
            if (roleEnum.getRole() == this.role) {
                return roleEnum.getAuthorities();
            }
        }
        return new ArrayList<>(0);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}