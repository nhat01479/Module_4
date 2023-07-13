package com.cg.model.dto.customer;

import com.cg.model.Customer;
import com.cg.model.dto.location.LocationRegionCreReqDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class CustomerCreReqDTO implements Validator {

    private String fullName;
    private String email;
    private String phone;
    private LocationRegionCreReqDTO locationRegion;

    public Customer toCustomer() {
        return new Customer()
                .setId(null)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(BigDecimal.ZERO)
                .setLocationRegion(locationRegion.toLocationRegion());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerCreReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        CustomerCreReqDTO customerCreReqDTO = (CustomerCreReqDTO) o;
        String fullName = customerCreReqDTO.fullName;
        String email = customerCreReqDTO.email;

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

    }
}
