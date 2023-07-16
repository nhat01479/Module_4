package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.dto.DepositCreReqDTO;
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
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/deposits")
public class DepositAPI {

    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> deposit(@PathVariable("customerId") String customerIdStr, @RequestBody DepositCreReqDTO depositCreReqDTO, BindingResult bindingResult) {
        Map<String, String> data = new HashMap<>();
        new DepositCreReqDTO().validate(depositCreReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        if (!validateUtils.isNumberValid(customerIdStr)) {
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long customerId = Long.parseLong(customerIdStr);

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Customer customer = customerOptional.get();

        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(depositCreReqDTO.getTransactionAmount()));

        Deposit deposit = new Deposit();
        deposit.setTransactionAmount(transactionAmount);
        deposit.setCustomer(customer);

        Customer newCustomer = customerService.deposit(deposit);
        CustomerResDTO customerResDTO = newCustomer.toCustomerResDTO();


        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }
}