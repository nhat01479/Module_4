package com.cg.model;

import com.cg.model.dto.customer.CustomerCreResDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.model.dto.customer.CustomerUpdateResDTO;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "customers")
@Accessors(chain = true)
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

   @Column(name = "full_name", nullable = false)
    private String fullName;
    @Column(nullable = false, unique = true)
    private String email;

    private String phone;

    @OneToOne
    @JoinColumn(name="location_region_id", referencedColumnName = "id", nullable = false)
    private LocationRegion locationRegion;

    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;
    public CustomerResDTO toCustomerResDTO(){
//        CustomerResDTO customerResDTO = new CustomerResDTO();
//        customerResDTO.setId(this.getId());
//        customerResDTO.setFullName(this.fullName);
//        customerResDTO.setEmail(this.email);
//        customerResDTO.setPhone(this.phone);
//        customerResDTO.setAddress(this.address);
//        customerResDTO.setBalance(this.balance);
//        return customerResDTO;
        return new CustomerResDTO()
                .setId(this.getId())
                .setFullName(this.fullName)
                .setEmail(this.email)
                .setPhone(this.phone)
                .setBalance(this.balance)
                .setLocationRegion(locationRegion.toLocationRegionDTO());
    }
    public CustomerCreResDTO toCustomerCreResDTO() {
        return new CustomerCreResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionCreResDTO())
                ;
    }
    public CustomerUpdateResDTO toCustomerUpdateResDTO() {
        return new CustomerUpdateResDTO()
                .setId(id)
                .setFullName(fullName)
                .setEmail(email)
                .setPhone(phone)
                .setBalance(balance)
                .setLocationRegion(locationRegion.toLocationRegionCreResDTO())
                ;
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return Customer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Customer customer = (Customer) target;
        String fullName = customer.fullName;
        String email = customer.email;
        String phone = customer.phone;
//        String address = customer.address;
        String regexName = "^[aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ][aAàÀảẢãÃáÁạẠăĂằẰẳẲẵẴắẮặẶâÂầẦẩẨẫẪấẤậẬbBcCdDđĐeEèÈẻẺẽẼéÉẹẸêÊềỀểỂễỄếẾệỆfFgGhHiIìÌỉỈĩĨíÍịỊjJkKlLmMnNoOòÒỏỎõÕóÓọỌôÔồỒổỔỗỖốỐộỘơƠờỜởỞỡỠớỚợỢpPqQrRsStTuUùÙủỦũŨúÚụỤưƯừỪửỬữỮứỨựỰvVwWxXyYỳỲỷỶỹỸýÝỵỴzZ ]{4,29}$";
        String regexEmail = "^[A-Za-z0-9._%+-]{5,15}@[A-Za-z0-9.-]{5,12}\\.[A-Za-z]{2,3}$";
        String phoneRegex = "^[0-9]{10}";
        if (fullName.length() == 0) {
            errors.rejectValue("fullName", "fullName.empty");
        } else {
            if (fullName.length() < 5 || fullName.length() > 30)
                errors.rejectValue("fullName", "fullName.length");
            else {
                if (!fullName.matches(regexName)) {
                    errors.rejectValue("fullName", "fullName.regex");
                }
            }
        }
        if (email.length() == 0) {
            errors.rejectValue("email", "email.empty");
        } else {
            if (!email.matches(regexEmail)) {
                errors.rejectValue("email", "email.regex");
            }
        }
    }


}
