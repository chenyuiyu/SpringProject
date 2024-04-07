package card.domain;

import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.Data;
import lombok.NonNull;

@Data
public class RegistrationForm {

    private @NonNull String username;
    private @NonNull String password;
    private String fullname;//用户名
    private String province;//省份
    private String city;//城市
    private String state;//区
    private String detailedLocation;//详细地址
    private String phoneNumber;//手机号码
    private String email;//邮箱
  
    public User toUser(PasswordEncoder passwordEncoder) {
        User user = new User(username, passwordEncoder.encode(password));
        user.setFullname(fullname);
        user.setProvince(province);
        user.setCity(city);
        user.setState(state);
        user.setDetailedLocation(detailedLocation);
        user.setPhoneNumber(phoneNumber);
        user.setEmail(email);
        return user;
    }
  
}