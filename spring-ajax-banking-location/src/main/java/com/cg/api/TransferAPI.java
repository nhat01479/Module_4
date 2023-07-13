package com.cg.api;

import com.cg.model.Customer;
import com.cg.model.Transfer;
import com.cg.model.dto.TransferCreReqDTO;
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

    @PostMapping("/{customerId}")
    public ResponseEntity<?> transfer(@PathVariable("customerId") String senderIdStr, @RequestBody TransferCreReqDTO transferCreReqDTO, BindingResult bindingResult) {
        Map<String, String> data = new HashMap<>();

        if (!validateUtils.isNumberValid(senderIdStr)) {
            data.put("message", "Mã khách hàng không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Long senderId = Long.valueOf(senderIdStr);

        Optional<Customer> optionalSender = customerService.findById(senderId);
        if (optionalSender.isEmpty()) {
            data.put("message", "Sender không hợp lệ");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer sender = optionalSender.get();

        Long recipientId = transferCreReqDTO.getRecipientId();
        Optional<Customer> optionalRecipient = customerService.findById(recipientId);
        if (optionalRecipient.isEmpty()) {
            data.put("message", "Không tìm thấy Recipient");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }
        Customer recipient = optionalRecipient.get();

        if (sender.getId() == recipient.getId()) {
            data.put("message", "Sender phải khác Recipient");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        new TransferCreReqDTO().validate(transferCreReqDTO, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            return appUtils.mapErrorToResponse(bindingResult);
        }

        BigDecimal currentBalance = sender.getBalance();
        Long transferAmountLong = Long.valueOf(transferCreReqDTO.getTransferAmount());
        BigDecimal transferAmount = BigDecimal.valueOf(transferAmountLong);
        Long fees = 10L;
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalance.compareTo(transactionAmount) < 0) {
            data.put("message", "Số dư không đủ để thực hiện giao dịch");
            return new ResponseEntity<>(data, HttpStatus.BAD_REQUEST);
        }

        Transfer transfer = new Transfer();
        transfer.setTransferAmount(transferAmount);
        transfer.setTransactionAmount(transactionAmount);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
//        transfer.setFees(10L);
        transfer.setFeesAmount(feesAmount);
        customerService.transfer(transfer);

        List<Customer> customers = new ArrayList<>();
        customers.add(transfer.getSender());
        customers.add(transfer.getRecipient());

        return new ResponseEntity<>(customers, HttpStatus.OK);

    }
}
