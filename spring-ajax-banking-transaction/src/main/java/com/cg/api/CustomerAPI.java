package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.dto.CustomerResDTO;
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
        List<Customer> customers = customerService.findAllByDeletedIs(false);
        List<CustomerResDTO> customerResDTOS = new ArrayList<>();
        for (Customer c : customers) {
            customerResDTOS.add(c.parse());
        }
        return ResponseEntity.ok(customerResDTOS);
    }
    @PostMapping
    public ResponseEntity<?> create(@RequestBody CustomerResDTO customerResDTO, BindingResult bindingResult) {
        new CustomerResDTO().validate(customerResDTO, bindingResult);

        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        Map<String, String> data = new HashMap<>();

        Customer customer = new Customer();


        String email = customerResDTO.getEmail();
        if (customerService.existsByEmail(email)) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } else {
            customer.setFullName(customerResDTO.getFullName());
            customer.setEmail(email);
            customer.setPhone(customerResDTO.getPhone());
            customer.setAddress(customerResDTO.getAddress());
            customer.setBalance(customerResDTO.getBalance());

            customerService.save(customer);

            customerResDTO.setId(customer.getId());

            return ResponseEntity.ok(customerResDTO);
        }

//        (@RequestBody Customer customer, BindingResult bindingResult)
//        customer.setId(null);
//        customer.setBalance(BigDecimal.ZERO);
//        Customer newCustomer = customerService.save(customer);
//
//        return new ResponseEntity<>(newCustomer, HttpStatus.OK);
    }
    @GetMapping("/{customerId}")
    public ResponseEntity<?> getById(@PathVariable String customerId){
        Optional<Customer> optionalCustomer = customerService.findById(Long.valueOf(customerId));
        if (optionalCustomer.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "ID không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = optionalCustomer.get();

        return new ResponseEntity<>(customer, HttpStatus.OK);
    }
    @PatchMapping("/{customerId}")
    public ResponseEntity<?> update(@PathVariable Long customerId, @RequestBody CustomerResDTO customerResDTO, BindingResult bindingResult) {

        Optional<Customer> optionalCustomer = customerService.findById(customerId);
        Map<String, String> data = new HashMap<>();

        if (optionalCustomer.isEmpty()) {
            data.put("message", "ID không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = optionalCustomer.get();

        new CustomerResDTO().validate(customerResDTO, bindingResult);

        if (bindingResult.hasFieldErrors()){
            return appUtils.mapErrorToResponse(bindingResult);
        }

        String newEmail = customerResDTO.getEmail();

        if (customerService.existsByEmailAndIdNot(newEmail, customer.getId())) {
            data.put("message", "Email đã tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        } else {
            customer.setFullName(customerResDTO.getFullName());
            customer.setEmail(newEmail);
            customer.setPhone(customerResDTO.getPhone());
            customer.setAddress(customerResDTO.getAddress());

            customerService.save(customer);

            return ResponseEntity.ok(customer);
        }
//        (@RequestBody Customer customer, BindingResult bindingResult)
//        customer.setId(null);
//        customer.setBalance(BigDecimal.ZERO);
//        Customer newCustomer = customerService.save(customer);
//
//        return new ResponseEntity<>(newCustomer, HttpStatus.OK);
    }

    @PatchMapping("/delete/{customerId}")
    public ResponseEntity<?> delete(@PathVariable Long customerId) {
        Optional<Customer> optionalCustomer = customerService.findById(Long.valueOf(customerId));
        if (optionalCustomer.isEmpty()) {
            Map<String, String> data = new HashMap<>();
            data.put("message", "ID không tồn tại");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer customer = optionalCustomer.get();
        customerService.softDeleteById(customerId);

        return new ResponseEntity<>(customer, HttpStatus.OK);

    }
}