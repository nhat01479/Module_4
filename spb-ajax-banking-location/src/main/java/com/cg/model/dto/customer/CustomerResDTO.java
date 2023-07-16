package com.cg.model.dto.customer;

import com.cg.model.LocationRegion;
import com.cg.model.dto.location.LocationRegionDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Accessors(chain = true)
public class CustomerResDTO implements Validator {

    private Long id;
    private String fullName;
    private String email;
    private String phone;
    private BigDecimal balance;

    private LocationRegionDTO locationRegion;
    public CustomerResDTO (Long id, String fullName, String email, String phone, BigDecimal balance, LocationRegion locationRegion) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.balance = balance;
        this.locationRegion = locationRegion.toLocationRegionDTO();
    }


    @Override
    public boolean supports(Class<?> aClass) {
        return CustomerResDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
//        CustomerResDTO customerResDTO = (CustomerResDTO) o;
//        String fullName = customerResDTO.fullName;
//        String email = customerResDTO.email;
//
//        if (fullName == null || fullName.trim().length() == 0) {
//            errors.rejectValue("fullName", "fullName.length", "Tên không được để trống");
//        } else {
//            if (!fullName.matches("([A-Z]+[a-z]*[ ]?)+$")) {
//                errors.rejectValue("fullName", "fullName.number", "Tên bắt đầu bằng chữ hoa, không được chứa số và ký tự đặc biệt");
//            } else {
//                if (fullName.trim().length() < 5 || fullName.trim().length() > 20) {
//                    errors.rejectValue("fullName", "fullName.length", "Tên từ 5-20 ký tự");
//                }
//            }
//        }
//
//
//        if (email == null || email.trim().length() == 0) {
//            errors.rejectValue("email", "email.length", "Email không được để trống");
//        } else {
//            if (!email.matches("[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,9}$")) {
//                errors.rejectValue("email", "email.regex", "Email không hợp lệ");
//            } else {
//                if (email.trim().length() > 30) {
//                    errors.rejectValue("email", "email.maxlength", "Email tối đa 30 ký tự");
//                }
//            }
//        }

    }
}
