package com.cg.model;

import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.Date;

@Entity
@Table(name = "customers")
public class Customer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
//    @NotBlank(message = "Ho ten la bat buoc")
//    @Size(min=6, max=20, message = "Tên từ 6-20 ký tự")
//    @Pattern(regexp = "^[A-Z][A-Za-z ]{5,19}$",message = "Tên phải đầu bằng chữ in hoa, từ 6-20 ký tự, không chứa số")
    @Column(name = "full_name", nullable = false)
    private String fullName;
//    @NotBlank(message = "Email là bắt buộc")
    @Column(nullable = false, unique = true)
//    @Unique(message = "Email đã tồn tại", domainClass = Customer.class, fieldName = "email")
//    @Pattern(regexp = "^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\\\.[A-Za-z]{2,3}$", message = "Email không hợp lệ")
    private String email;
//    @Unique(message = "SĐT đã tồn tại", domainClass = Customer.class, fieldName = "phone")
//    @Pattern(regexp = "[0][3|5|7|8|9][0-9]{8}", message = "SDT không hợp lệ")
    private String phone;
    private String address;
    @Column(precision = 10, scale = 0, nullable = false, updatable = false)
    private BigDecimal balance;

    public Customer() {
    }

    public Customer(Long id, String fullName, String email, String phone, String address, BigDecimal balance) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.balance = balance;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
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
        String address = customer.address;
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
//        if (!phone.matches(regexEmail)) {
//            errors.rejectValue("phone", "phone.regex");
//        }
        if (phone.length() > 10) {
            errors.rejectValue("phone", "phone.length");
        }
        if (address.length() > 30) {
            errors.rejectValue("address", "address.length");
        }




    }
}
