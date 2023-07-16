package com.cg.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

import javax.persistence.*;
import java.math.BigDecimal;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Transactional
@Table(name = "transfers")
public class Transfer extends BaseEntity implements Validator {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "sender_id", referencedColumnName = "id", nullable = false)
    private Customer sender;
    @ManyToOne
    @JoinColumn(name = "recipient_id", referencedColumnName = "id", nullable = false)
    private Customer recipient;
    @Column(name = "transfer_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transferAmount;
    private Long fees = 10L;
    @Column(name = "fees_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal feesAmount;
    @Column(name = "transaction_amount", precision = 10, scale = 0, nullable = false)
    private BigDecimal transactionAmount;


    @Override
    public boolean supports(Class<?> aClass) {
        return Transfer.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object target, Errors errors) {

        Transfer transfer = (Transfer) target;

        BigDecimal transferAmount = transfer.getTransferAmount();

        if (transferAmount == null) {
            errors.rejectValue("transferAmount", "transfer.zero");
        } else {
            if (transferAmount.compareTo(BigDecimal.valueOf(0)) <= 0) {
                errors.rejectValue("transferAmount", "transfer.min");
            } else {
                if (transferAmount.compareTo(BigDecimal.valueOf(500000000)) > 0) {
                    errors.rejectValue("transferAmount", "transfer.max");
                }
            }
        }

    }

}
