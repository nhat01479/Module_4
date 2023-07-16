package com.cg.service.customer;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.model.dto.TransferCreReqDTO;
import com.cg.model.dto.customer.*;
import com.cg.service.IGeneralService;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ICustomerService extends IGeneralService<Customer, Long> {
    List<Customer> findAllByIdNot(Long id);
    List<CustomerResDTO> findAllCustomerResDTO();
    List<CustomerResDTO> findAllRecipientsWithoutSenderId(Long senderId);
    Boolean existsByEmail(String email);
    Boolean existsByEmailAndIdNot(String email, Long id);
    Boolean existsByEmailNot(String email);
    void softDeleteById(Long id);
    List<Customer> findAllByDeletedIs(Boolean boo);
    Customer deposit(Deposit deposit);
    Customer withdraw(Withdraw withdraw);
    void transfer(TransferCreReqDTO transferCreReqDTO);
    void transfer(Transfer transfer);

    CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO);
    CustomerUpdateResDTO update(CustomerUpdateReqDTO customerUpdateReqDTO, Customer customer);
//    void incrementBalance(Long id, BigDecimal amount);
//    void  decrementBalance(Long id, BigDecimal amount);

}
