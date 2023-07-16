package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.TransferCreReqDTO;
import com.cg.model.dto.TransferCreResDTO;
import com.cg.model.dto.customer.CustomerResDTO;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.*;

@RestController
@RequestMapping("api/transfers")
public class TransferAPI {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @PostMapping
    public ResponseEntity<?> transfer( @RequestBody TransferCreReqDTO transferCreReqDTO, BindingResult bindingResult) {
        Map<String, String> data = new HashMap<>();

        new TransferCreReqDTO().validate(transferCreReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Long senderId = transferCreReqDTO.getSenderId();
        Long recipientId = transferCreReqDTO.getRecipientId();

        if (!validateUtils.isNumberValid(String.valueOf(senderId))) {
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        if (senderId == recipientId) {
            data.put("message", "Sender phải khác Recipient");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        customerService.transfer(transferCreReqDTO);

        TransferCreResDTO transferCreResDTO = new TransferCreResDTO();

        Customer sender = customerService.findById(senderId).get();
        Customer recipient = customerService.findById(recipientId).get();

        transferCreResDTO.setSender(sender.toCustomerResDTO());
        transferCreResDTO.setRecipient(recipient.toCustomerResDTO());
        System.out.println("Sau: " + sender.getBalance());

//        Map<String, CustomerResDTO> customers = new HashMap<>();
//        customers.put("sender", sender.toCustomerResDTO());
//        customers.put("recipient", recipient.toCustomerResDTO());
//
//        return new ResponseEntity<>(customers, HttpStatus.OK);

        return new ResponseEntity<>(transferCreResDTO, HttpStatus.OK);

    }
}
