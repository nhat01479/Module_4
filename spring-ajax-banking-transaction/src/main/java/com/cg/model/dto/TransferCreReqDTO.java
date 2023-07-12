package com.cg.model.dto;

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
    }
}
