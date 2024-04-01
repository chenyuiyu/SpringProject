package card.data.domain;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.validation.constraints.Size;

import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import lombok.NonNull;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@Table("MyUser")
public class User implements UserDetails {
    
    private static final long serialVersionUID = 1L;

    @Id
    private Long id;

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

    private Set<String> roles = new HashSet<>(Arrays.asList("ROLE_USER"));

    //授权集合
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> res = new ArrayList<>();
        for(String ga : roles) res.add(new SimpleGrantedAuthority(ga));
        return res;
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
}
