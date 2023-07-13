package com.cg.api;

import com.cg.exception.DataInputException;
import com.cg.exception.EmailExistsException;
import com.cg.model.Customer;
import com.cg.model.dto.customer.*;
import com.cg.service.customer.ICustomerService;
import com.cg.utils.AppUtils;
import com.cg.utils.ValidateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@RestController
@RequestMapping("/api/customers")
public class CustomerAPI {
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private AppUtils appUtils;
    @Autowired
    private ValidateUtils validateUtils;
    @GetMapping
    public ResponseEntity<?> getAllCustomers() {
//        List<Customer> customers = customerService.findAllByDeletedIs(false);
//        List<CustomerResDTO> customerResDTOS = new ArrayList<>();
//
//        for (Customer c : customers) {
//            CustomerResDTO customerDTO = c.toCustomerResDTO();
//            customerResDTOS.add(customerDTO);
//        }
        List<CustomerResDTO> customerResDTOS = customerService.findAllCustomerResDTO();
        return ResponseEntity.ok(customerResDTOS);
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerCreReqDTO customerCreReqDTO, BindingResult bindingResult) {

        Map<String, String> data = new HashMap<>();

        new CustomerCreReqDTO().validate(customerCreReqDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }
        String email = customerCreReqDTO.getEmail();
        if (customerService.existsByEmail(email)){
//            throw new EmailExistsException("Email đã tồn tại");
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        CustomerCreResDTO customerCreResDTO = customerService.create(customerCreReqDTO);

        return new ResponseEntity<>(customerCreResDTO, HttpStatus.CREATED);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable String customerId){
        Customer customer = customerService.findById(Long.valueOf(customerId)).orElseThrow(() ->
                new DataInputException("Mã khách hàng không tồn tại"));

        CustomerResDTO customerResDTO = customer.toCustomerResDTO();

        return new ResponseEntity<>(customerResDTO, HttpStatus.OK);
    }
    @PatchMapping("/{customerId}")
    public ResponseEntity<?> update(@PathVariable String customerId, @RequestBody CustomerUpdateReqDTO customerUpdateReqDTO, BindingResult bindingResult) {

        Customer customer = customerService.findById(Long.valueOf(customerId)).orElseThrow(() ->
                new DataInputException("Mã khách hàng không tồn tại"));

        new CustomerResDTO().validate(customerUpdateReqDTO, bindingResult);

        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        String email = customer.getEmail();
        if (customerService.existsByEmailNot(email)){
            throw new EmailExistsException("Email đã tồn tại");
        }

        CustomerUpdateResDTO customerUpdateResDTO = customerService.update(customerUpdateReqDTO);

        return new ResponseEntity<>(customerUpdateResDTO, HttpStatus.OK);

    }

    @PatchMapping("/delete/{customerId}")
    public ResponseEntity<?> delete(@PathVariable String customerId) {
        Customer customer = customerService.findById(Long.valueOf(customerId)).orElseThrow(() ->
                new DataInputException("Mã khách hàng không tồn tại"));

        customerService.softDeleteById(Long.valueOf(customerId));


        return new ResponseEntity<>(customer.toCustomerResDTO(), HttpStatus.OK);

    }
}