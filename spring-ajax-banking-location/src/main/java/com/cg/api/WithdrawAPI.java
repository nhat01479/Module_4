package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Withdraw;
import com.cg.model.dto.WithdrawCreReqDTO;
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
        Map<String, String> data = new HashMap<>();

        new WithdrawCreReqDTO().validate(withdrawCreReqDTO, bindingResult);

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

        BigDecimal transactionAmount = BigDecimal.valueOf(Long.parseLong(withdrawCreReqDTO.getTransactionAmount()));

        if (customer.getBalance().compareTo(transactionAmount) < 0) {
            data.put("message", "Số dư không đủ để thực hiện giao dịch");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Withdraw withdraw = new Withdraw();
        withdraw.setTransactionAmount(transactionAmount);
        withdraw.setCustomer(customer);

        Customer newCustomer = customerService.withdraw(withdraw);
        CustomerResDTO customerResDTO = newCustomer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);

    }
}
