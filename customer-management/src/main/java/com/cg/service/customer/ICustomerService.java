package com.cg.service.customer;

import com.cg.model.Customer;

import java.util.List;

public interface ICustomerService {
    List<Customer> getAll();

    Customer getById(Long id);

    void add(Customer customer);

    void update(Customer customer);
}
