package card.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NonNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Document
public class User implements UserDetails {
    
    private static final long serialVersionUID = 1L;

    @Id
    private @NonNull String username;//用户凭证
    private @NonNull String password;//密码

    @Size(max = 50, message = "用户名不能超过50个字符")
    private String fullname;//用户名
    private String province;//省份
    private String city;//城市
    private String state;//区
    private String detailedLocation;//详细地址
    private String phoneNumber;//手机号码
    private String email;//邮箱

    private Set<GrantedAuthority> roles = new HashSet<>(Arrays.asList(new SimpleGrantedAuthority("ROLE_USER")));

    public void addRole(GrantedAuthority grand) {
        if(grand != null) roles.add(grand);
    }

    public void addRoles(Collection<? extends GrantedAuthority> grands) {
        for(GrantedAuthority grand : grands) this.addRole(grand);
    }

    //授权集合
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles;
    }

    //账号未过期确认
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    //账号未被锁定
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

    public UserDetails toUserDetails() {
        return this;
    }
}
