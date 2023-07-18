package com.cg.model.dto.user;


import com.cg.model.Role;
import com.cg.model.User;
import com.cg.model.dto.DepositCreReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserRegisterReqDTO implements Validator {

    private String username;
    private String password;
    private String rePassword;
    private Long roleId;

    public User toUser(Role role) {
        return new User()
                .setUsername(username)
                .setPassword(password)
                .setRole(role)
                ;
    }

    @Override
    public boolean supports(Class<?> clazz) {
        return UserRegisterReqDTO.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object target, Errors errors) {
        UserRegisterReqDTO userRegisterReqDTO = (UserRegisterReqDTO) target;
        String userName = userRegisterReqDTO.getUsername();
        String password = userRegisterReqDTO.getRePassword();
        String rePassword = userRegisterReqDTO.getPassword();

        if (userName == null || userName.trim().length() == 0) {
            errors.rejectValue("username", "username.length", "Username không được để trống");
            return;
        }
        if (password == null || password.trim().length() == 0) {
            errors.rejectValue("password", "password.length", "Password không được để trống");
            return;
        }
        if (password.trim().length() > 20) {
            errors.rejectValue("password", "password.maxLength", "Password không quá 20 ký tự");
        }
        if (rePassword == null || rePassword.trim().length() == 0) {
            errors.rejectValue("rePassword", "rePassword.length", "RePassword không được để trống");
            return;
        }
        if (!userName.matches("^[a-z][a-z0-9]{5,15}$")){
            errors.rejectValue("username", "username.regex", "Username bắt đầu bằng a-z, có thể chứa số, từ 6-16 ký tự");
        }


    }
}
