package com.cg.service.customer;

import com.cg.exception.DataInputException;
import com.cg.model.*;
import com.cg.model.dto.TransferCreReqDTO;
import com.cg.model.dto.customer.*;
import com.cg.model.dto.location.LocationRegionCreReqDTO;
import com.cg.model.dto.location.LocationRegionCreResDTO;
import com.cg.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
@Service
@Transactional
public class CustomerServiceImpl implements ICustomerService {
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private DepositRepository depositRepository;
    @Autowired
    private WithdrawRepository withdrawRepository;
    @Autowired
    private TransferRepository transferRepository;
    @Autowired
    private LocationRegionRepository locationRegionRepository;
    @Override
    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    @Override
    public List<Customer> findAllByIdNot(Long id) {
        return customerRepository.findAllByIdNot(id);
    }

    @Override
    public List<CustomerResDTO> findAllCustomerResDTO() {
        return  customerRepository.findAllCustomerResDTO();
    }

    @Override
    public List<Customer> findCustomerByFullNameLikeAndDeletedFalse(String name) {
        return customerRepository.findCustomerByFullNameLikeAndDeletedFalse(name);
    }

    @Override
    public List<CustomerResDTO> findAllRecipientsWithoutSenderId(Long senderId) {
        return customerRepository.findAllRecipientsWithoutSenderId(senderId);
    }

    @Override
    public CustomerCreResDTO create(CustomerCreReqDTO customerCreReqDTO) {

        LocationRegionCreReqDTO locationRegionCreReqDTO = customerCreReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customer = customerCreReqDTO.toCustomer();
        customer.setLocationRegion(locationRegion);
        customerRepository.save(customer);

        CustomerCreResDTO customerCreResDTO = customer.toCustomerCreResDTO();
        return customerCreResDTO;
    }
    @Override
    public CustomerUpdateResDTO update(CustomerUpdateReqDTO customerUpdateReqDTO, Customer customer) {

        LocationRegionCreReqDTO locationRegionCreReqDTO = customerUpdateReqDTO.getLocationRegion();
        LocationRegion locationRegion = locationRegionCreReqDTO.toLocationRegion();
        locationRegionRepository.save(locationRegion);

        Customer customerUpdate = customerUpdateReqDTO.toCustomer(customer);
        customerUpdate.setLocationRegion(locationRegion);
        customerUpdate.setBalance(customer.getBalance());
        customerRepository.save(customerUpdate);

        return customerUpdate.toCustomerUpdateResDTO();
    }

    public List<Customer> findAllByDeletedIs(Boolean boo) {
        return customerRepository.findAllByDeletedIs(boo);
    }

    @Override
    public Optional<Customer> findById(Long id) {
        return customerRepository.findById(id);
    }
    public Boolean existsByEmail(String email) {
        return customerRepository.existsByEmail(email);
    }
    public  Boolean existsByEmailAndIdNot(String email, Long id){
        return customerRepository.existsByEmailAndIdNot(email, id);
    }
    public Boolean existsByEmailNot(String email) {
        return customerRepository.existsByEmailNot(email);
    }


//    @Override
//    public void incrementBalance(Long id, BigDecimal amount) {
//        customerRepository.incrementBalance(id, amount);
//    }
//
//    @Override
//    public void decrementBalance(Long id, BigDecimal amount) {
//        customerRepository.incrementBalance(id, amount);
//    }

    @Override
    public Customer deposit(Deposit deposit) {
        deposit.setId(null);
        depositRepository.save(deposit);

        Customer customer = deposit.getCustomer();
        BigDecimal transactionAmount = deposit.getTransactionAmount();

        customerRepository.incrementBalance(customer.getId(), transactionAmount);

        BigDecimal newBalance = customer.getBalance().add(transactionAmount);
        customer.setBalance(newBalance);

        return customer;
    }
    @Override
    public Customer withdraw(Withdraw withdraw) {
        withdraw.setId(null);
        withdrawRepository.save(withdraw);

        Customer customer = withdraw.getCustomer();
        BigDecimal transactionAmount = withdraw.getTransactionAmount();

        customerRepository.decrementBalance(customer.getId(), transactionAmount);
        BigDecimal newBalance = customer.getBalance().subtract(transactionAmount);
        customer.setBalance(newBalance);

        return customer;
    }

    public void transfer(Transfer transfer) {
        BigDecimal transferAmount = transfer.getTransferAmount();
        BigDecimal transactionAmount = transfer.getTransactionAmount();

        customerRepository.decrementBalance(transfer.getSender().getId(), transactionAmount);

        customerRepository.incrementBalance(transfer.getRecipient().getId(), transferAmount);

        transferRepository.save(transfer);

        Customer sender = customerRepository.findById(transfer.getSender().getId()).get();
        Customer recipient = customerRepository.findById(transfer.getRecipient().getId()).get();
        transfer.setSender(sender);
        transfer.setRecipient(recipient);



    }

    @Override
    public void transfer(TransferCreReqDTO transferCreReqDTO) {

        Long senderId = transferCreReqDTO.getSenderId();
        Long recipientId = transferCreReqDTO.getRecipientId();

        Customer sender = customerRepository.findById(senderId).orElseThrow(() -> {
            throw new DataInputException("Sender không tồn tại");
        });

        Customer recipient = customerRepository.findById(recipientId).orElseThrow(() -> {
            throw new DataInputException("Recipient không tồn tại");
        });
        System.out.println("Truoc: " + sender.getBalance());

        BigDecimal currentBalance = sender.getBalance();
        Long transferAmountLong = Long.valueOf(transferCreReqDTO.getTransferAmount());
        BigDecimal transferAmount = BigDecimal.valueOf(transferAmountLong);
        Long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalance.compareTo(transactionAmount) < 0) {
            throw new DataInputException("Số dư không đủ để thực hiện giao dịch");
        }
        customerRepository.decrementBalance(senderId, transactionAmount);
        customerRepository.incrementBalance(recipientId, transferAmount);


        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        transfer.setTransactionAmount(transactionAmount);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
        transfer.setFees(fees);
        transfer.setFeesAmount(feesAmount);
        transferRepository.save(transfer);

    }

    @Override
    public Customer save(Customer customer) {
        return customerRepository.save(customer);
    }
    @Override
    public void softDeleteById(Long id) {
        customerRepository.softDeleteById(id);

    }
//    @Override
//    public void softDelete(Customer customer) {
//        customer.setDeleted(true);
//        customerRepository.save(customer);
//    }

    @Override
    public void delete(Customer customer) {
        customerRepository.delete(customer);
    }

    @Override
    public void deleteById(Long id) {
        customerRepository.deleteById(id);
    }

}
