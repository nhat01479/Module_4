package com.cg.model.dto;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.utils.ValidateUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.Column;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerResDTO implements Validator {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private String address;
    private BigDecimal balance;


    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerResDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomerResDTO customerResDTO = (CustomerResDTO) o;
        String fullName = customerResDTO.fullName;
        String email = customerResDTO.email;
        String phone = customerResDTO.email;
        String address = customerResDTO.email;

        if (fullName == null || fullName.trim().length() == 0) {
            errors.rejectValue("fullName", "fullName.length", "Tên không được để trống");
        } else {
            if (!fullName.matches("([A-Z]+[a-z]*[ ]?)+$")) {
                errors.rejectValue("fullName", "fullName.number", "Tên bắt đầu bằng chữ hoa, không được chứa số và ký tự đặc biệt");
            } else {
                if (fullName.trim().length() < 5 || fullName.trim().length() > 20) {
                    errors.rejectValue("fullName", "fullName.length", "Tên từ 5-20 ký tự");
                }
            }
        }


        if (email == null || email.trim().length() == 0) {
            errors.rejectValue("email", "email.length", "Email không được để trống");
        } else {
            if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,9}$")) {
                errors.rejectValue("email", "email.regex", "Email không hợp lệ");
            } else {
                if (email.trim().length() > 30) {
                    errors.rejectValue("email", "email.maxlength", "Email tối đa 30 ký tự");
                }
            }
        }
//        if (phone.trim().length() > 15) {
//            errors.rejectValue("phone", "phone.length", "Phone không quá 15 ký tự");
//        }
//        if (address.trim().length() > 20) {
//            errors.rejectValue("address", "address.length", "Địa chỉ không quá 20 ký tự");
//        }
    }
}
