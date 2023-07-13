package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "withdraws")
public class Withdraw extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customerId", referencedColumnName = "id", nullable = false)
    private Customer customer;
    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return Withdraw.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {
        Withdraw withdraw = (Withdraw) target;
        BigDecimal transactionAmount = withdraw.getTransactionAmount();
        if (transactionAmount == null) {
            errors.rejectValue("transactionAmount", "withdraw.zero");
        } else {
            if (transactionAmount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                errors.rejectValue("transactionAmount", "withdraw.min");
            } else {
                if (transactionAmount.compareTo(BigDecimal.valueOf(500000000)) > 0) {
                    errors.rejectValue("transactionAmount", "withdraw.max");
                }
            }
        }


    }
}
