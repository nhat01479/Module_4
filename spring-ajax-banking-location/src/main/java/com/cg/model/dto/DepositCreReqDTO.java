package com.cg.model.dto;

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
public class DepositCreReqDTO implements Validator {

    private String transactionAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return DepositCreReqDTO.class.isAssignableFrom(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        DepositCreReqDTO depositCreReqDTO = (DepositCreReqDTO) o;

        String transactionAmountStr = depositCreReqDTO.transactionAmount;

        if (transactionAmountStr == null || transactionAmountStr.trim().length() == 0) {
            errors.rejectValue("transactionAmount", "transactionAmount.length", "Số tiền cần nạp không được để trống");
            return;
        }
        if (!transactionAmountStr.matches("\\d+")) {
            errors.rejectValue("transactionAmount", "transactionAmount.regex", "Số tiền không đúng định dạng");
        } else {
            BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(transactionAmountStr));

            if (transactionAmount.compareTo(BigDecimal.valueOf(1000L)) < 0) {
                errors.rejectValue("transactionAmount", "transactionAmount.min", "Số tiền nạp thấp nhất là 1000");
            } else {
                if (transactionAmount.compareTo(BigDecimal.valueOf(100000000L)) > 0) {
                    errors.rejectValue("transactionAmount", "transactionAmount.max", "Số tiền muốn nạp tối đa là 100.000.000");
                }
            }
        }

    }
}
