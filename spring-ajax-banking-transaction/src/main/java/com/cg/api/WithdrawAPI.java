package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Withdraw;
import com.cg.model.dto.DepositCreReqDTO;
import com.cg.model.dto.WithdrawCreReqDTO;
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
@RequestMapping("/api/withdraws")
public class WithdrawAPI {
    @Autowired
    private ICustomerService customerService;

    @Autowired
    private AppUtils appUtils;

    @Autowired
    private ValidateUtils validateUtils;

    @PostMapping("/{customerId}")
    public ResponseEntity<?> withdraw(@PathVariable("customerId") String customerIdStr, @RequestBody WithdrawCreReqDTO withdrawCreReqDTO, BindingResult bindingResult) {
        new WithdrawCreReqDTO().validate(withdrawCreReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        if (!validateUtils.isNumberValid(customerIdStr)) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long customerId = Long.parseLong(customerIdStr);

        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "Mã khách hàng không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = customerOptional.get();

        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(withdrawCreReqDTO.getTransactionAmount()));

        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(transactionAmount);
        withdraw.setCustomer(customer);

        Customer newCustomer = customerService.withdraw(withdraw);



        return new ResponseEntity<>(newCustomer, HttpStatus.OK);

    }
}
