package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService{
    @Autowired
    private CustomerRepository customerRepository;
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

    @Override
    public List<Customer> findAllByFullNameLike(String fullName) {
        return customerRepository.findAllByFullNameLike(fullName);
    }

    @Override
    public List<Customer> findAllByFullNameLikeOrEmailLikeOrPhoneLike(String fullName, String email, String phone) {
        return customerRepository.findAllByFullNameLikeOrEmailLikeOrPhoneLike(fullName, email, phone);
    }
}
