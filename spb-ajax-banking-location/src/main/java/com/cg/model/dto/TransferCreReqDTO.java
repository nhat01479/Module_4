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
public class TransferCreReqDTO implements Validator {
    private Long senderId;
    private Long recipientId;
    private String transferAmount;

    @Override
    public boolean supports(Class<?> aClass) {
        return TransferCreReqDTO.class.isAssignableFrom(aClass);
    }
    @Override
    public void validate(Object o, Errors errors) {

        TransferCreReqDTO transferCreReqDTO = (TransferCreReqDTO) o;

        String transferStr = transferCreReqDTO.getTransferAmount();

        if (transferStr == null || transferStr.trim().length() == 0) {
            errors.rejectValue("transferAmount", "transferAmount", "Không được để trống");
            return;
        }
        if (!transferStr.matches("\\d+")) {
            errors.rejectValue("transferAmount", "transferAmount.regex", "Số tiền không đúng định dạng");
        } else {
            BigDecimal transferAmount = BigDecimal.valueOf(Long.parseLong(transferStr));

            if (transferAmount.compareTo(BigDecimal.valueOf(1000)) < 0) {
                errors.rejectValue("transferAmount", "transferAmount.min", "Số tiền chuyển tối thiểu là 1.000");
            } else {
                if (transferAmount.compareTo(BigDecimal.valueOf(100000000)) > 0) {
                    errors.rejectValue("transferAmount", "transferAmount.max", "Số tiền chuyển tối đa là 100.000.000");
                }
            }
        }

    }
}
