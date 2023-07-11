package com.cg.controller;

import com.cg.model.Customer;
import com.cg.model.Deposit;
import com.cg.model.Transfer;
import com.cg.model.Withdraw;
import com.cg.service.customer.ICustomerService;
import com.cg.service.deposit.IDepositService;
import com.cg.service.transfer.ITransferService;
import com.cg.service.withdraw.IWithdrawService;
import com.cg.utils.AppUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/customers")
public class CustomerController {
    @Autowired
    private AppUtils appUtils;
    @Autowired
    private ICustomerService customerService;
    @Autowired
    private IDepositService depositService;
    @Autowired
    private IWithdrawService withdrawService;
    @Autowired
    private ITransferService transferService;

    @GetMapping
    public String showListPage(Model model) {
        List<Customer> customers = customerService.findAllByDeletedIs(false);

        model.addAttribute("customers", customers);

        return "customer/list";
    }

    @GetMapping("/create")
    public String showCreatePage(Model model) {
        model.addAttribute("customer", new Customer());
        return "/customer/create";
    }

    @PostMapping("/create")
    public String doCreate(Model model, @ModelAttribute Customer customer, BindingResult bindingResult) {
        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "/customer/create";
        }
        String email = customer.getEmail();
        Boolean existEmail = customerService.existsByEmail(email);
        if (existEmail) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "/customer/create";
        }
        customer.setId(null);
        customer.setBalance(BigDecimal.ZERO);
        customerService.save(customer);
        model.addAttribute("success", true);
        model.addAttribute("message", "Thêm khách hàng thành công");
        model.addAttribute("customer", new Customer());
        return "/customer/create";


    }

    @GetMapping("/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(Long.valueOf(id));

            if (customerOptional.isEmpty()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Không tìm thấy");

            } else {
                Customer customer = customerOptional.get();
                model.addAttribute("customer", customer);
            }

        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không hợp lệ");
        }
        return "/customer/edit";

    }

    @PostMapping("/{id}")
    public String doUpdate(@PathVariable Long id, @ModelAttribute Customer customer, Model model, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(id);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy");
            return "/customer/edit";
        }

        new Customer().validate(customer, bindingResult);

        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("hasError", true);
            return "/customer/edit";
        }
        String email = customer.getEmail();
        Boolean existEmail = customerService.existsByEmailAndIdNot(email, customer.getId());
        if (existEmail) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Email đã tồn tại");
            return "/customer/edit";
        }

        Customer c = customerOptional.get();
        customer.setId(id);
        customer.setBalance(c.getBalance());
        customerService.save(customer);
        model.addAttribute("customer", customer);

        model.addAttribute("success", true);
        model.addAttribute("message", "Cập nhật thành công");

        return "/customer/edit";


    }

    @GetMapping("/delete/{id}")
    public String doDelete(@PathVariable String id, RedirectAttributes redirectAttributes) {

        try {
            Long customerId = Long.parseLong(id);
            customerService.softDeleteById(customerId);

            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Xoá thành công");
            return "redirect:/customers";

        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", true);
            redirectAttributes.addFlashAttribute("message", "Không thể xoá");
            return "redirect:/customers";
        }

    }

    @GetMapping("/deposit/{customerId}")
    public String showDepositForm(@PathVariable String customerId, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(Long.valueOf(customerId));

            if (customerOptional.isEmpty()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "Không tìm thấy customer");

            } else {

                Customer customer = customerOptional.get();
                Deposit deposit = new Deposit();
                deposit.setCustomer(customer);

                model.addAttribute("deposit", deposit);
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không hợp lệ");
        }
        return "/customer/deposit";
    }

    @PostMapping("/deposit/{customerId}")
    public String doDeposit(@PathVariable Long customerId, Model model, @ModelAttribute Deposit deposit, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
            return "/customer/deposit";
        }
/**
 Customer customer = customerOptional.get();
 //add: cộng balance hiện tại với số tiền nhập vào
 BigDecimal currentBalance = customer.getBalance();
 BigDecimal newBalance = currentBalance.add(deposit.getTransactionAmount());
 customer.setBalance(newBalance);
 customerService.save(customer);

 deposit.setId(null);
 deposit.setCustomer(customer);
 depositService.save(deposit);
 **/

// Để việc xử lý tiền tệ trong service có @Transaction
        try {
            new Deposit().validate(deposit, bindingResult);
            if (bindingResult.hasFieldErrors()) {
                model.addAttribute("depositError", true);
                return "/customer/deposit";
            } else {
                Customer customer = customerService.deposit(deposit);
                deposit.setCustomer(customer);

                model.addAttribute("deposit", deposit);
                model.addAttribute("success", true);
                model.addAttribute("message", "Gửi thành công " + deposit.getTransactionAmount() + " $ vào tài khoản");
            }

        } catch (Exception e) {
            return "error/500";
        }

        return "/customer/deposit";
    }

    @GetMapping("/withdraw/{customerId}")
    public String showWithdrawForm(@PathVariable String customerId, Model model) {
        try {
            Optional<Customer> customerOptional = customerService.findById(Long.valueOf(customerId));

            if (customerOptional.isEmpty()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "ID không tồn tại");

            } else {
                Customer customer = customerOptional.get();
                Withdraw withdraw = new Withdraw();
                withdraw.setCustomer(customer);
                model.addAttribute("withdraw", withdraw);
            }

        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không hợp lệ");
        }
        return "/customer/withdraw";

    }

    @PostMapping("/withdraw/{customerId}")
    public String doWithdraw(@PathVariable Long customerId, Model model, @ModelAttribute Withdraw withdraw, BindingResult bindingResult) {
        Optional<Customer> customerOptional = customerService.findById(customerId);

        if (customerOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không tồn tại");
            return "customer/withdraw";
        }
        /**
         Customer customer = customerOptional.get();
         BigDecimal currentBalance = customer.getBalance();
         if (currentBalance.compareTo(withdraw.getTransactionAmount()) >= 0) {

         BigDecimal newBalance = currentBalance.subtract(withdraw.getTransactionAmount());
         customer.setBalance(newBalance);
         customerService.save(customer);

         withdraw.setId(null);
         withdraw.setCustomer(customer);
         withdrawService.save(withdraw);

         } else {
         model.addAttribute("error", true);
         model.addAttribute("message", "Số dư không đủ");
         withdraw.setCustomer(customer);
         }
         **/


        try {
            new Withdraw().validate(withdraw, bindingResult);
            if (bindingResult.hasFieldErrors()) {
                model.addAttribute("withdrawError", true);
                return "/customer/withdraw";
            } else {
                Customer customer = customerOptional.get();

                BigDecimal transactionAmount = withdraw.getTransactionAmount();

                if (customer.getBalance().compareTo(transactionAmount) >= 0) {

                    customer = customerService.withdraw(withdraw);
                    withdraw.setCustomer(customer);

                    model.addAttribute("success", true);
                    model.addAttribute("message", "Rút thành công: " + withdraw.getTransactionAmount() + " $");
                    model.addAttribute("withdraw", withdraw);

                } else {
                    model.addAttribute("error", true);
                    model.addAttribute("message", "Số dư không đủ");
                    withdraw.setCustomer(customer);
                }
            }

        } catch (Exception e) {
            return "error/500";
        }

        return "/customer/withdraw";
    }

    @GetMapping("/transfer/{senderId}")
    public String showTransferForm(@PathVariable String senderId, Model model) {

        try {
            Optional<Customer> senderOptional = customerService.findById(Long.valueOf(senderId));

            if (senderOptional.isEmpty()) {
                model.addAttribute("error", true);
                model.addAttribute("message", "ID không tồn tại");
            } else {
                Customer sender = senderOptional.get();

                Transfer transfer = new Transfer();
                transfer.setSender(sender);
                model.addAttribute("transfer", transfer);

                List<Customer> recipients = customerService.findAllByIdNot(sender.getId());
                model.addAttribute("recipients", recipients);
            }
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID không hợp lệ");
        }
        return "/customer/transfer";
    }

    @PostMapping("/transfer/{senderId}")
    public String doTransfer(@PathVariable String senderId, Model model, @RequestParam("recipient.id") String recipientId, @Validated @ModelAttribute Transfer transfer, BindingResult bindingResult) {
        Long recipientIdd;
        try {
            recipientIdd = Long.parseLong(recipientId);
        } catch (Exception e) {
            model.addAttribute("error", true);
            model.addAttribute("message", "ID recipient không đúng định dạng");
            model.addAttribute("currentRecipient", recipientId);
            List<Customer> recipients = customerService.findAll();
            model.addAttribute("recipients", recipients);
            return "/customer/transfer";
        }


        Optional<Customer> senderOptional = customerService.findById(Long.valueOf(senderId));
        List<Customer> recipients = customerService.findAll();
        model.addAttribute("recipients", recipients);
        model.addAttribute("transfer", transfer);

        if (senderOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy sender");
            return "/customer/transfer";
        }

        Optional<Customer> recipientOptional = customerService.findById(transfer.getRecipient().getId());

        if (recipientOptional.isEmpty()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Không tìm thấy recipient");
            return "/customer/transfer";
        }


        Customer sender = senderOptional.get();
        Customer recipient = recipientOptional.get();

        if (sender.getId() == recipient.getId()) {
            model.addAttribute("error", true);
            model.addAttribute("message", "Recipient phải khác Sender");
            return "/customer/transfer";
        }

        transfer.validate(transfer, bindingResult);
        if (bindingResult.hasFieldErrors()) {
            model.addAttribute("currentRecipient", recipientIdd);
            model.addAttribute("transferError", true);
            return "/customer/transfer";
        }


//        if (transfer.getTransferAmount().compareTo(BigDecimal.ZERO) <= 0) {
//            model.addAttribute("error", true);
//            model.addAttribute("message", "Số tiền chuyển phải > 0");
//            return "/customer/transfer";
//        }
//        if (transfer.getTransferAmount().compareTo(BigDecimal.valueOf(500000000)) > 0) {
//            model.addAttribute("error", true);
//            model.addAttribute("message", "Số tiền chuyển phải <= 500.000.000");
//            return "/customer/transfer";
//        }


        BigDecimal currentBalance = sender.getBalance();
        BigDecimal transferAmount = transfer.getTransferAmount();
        Long fees = transfer.getFees();
        BigDecimal feesAmount = transferAmount.multiply(BigDecimal.valueOf(fees)).divide(BigDecimal.valueOf(100));
        BigDecimal transactionAmount = transferAmount.add(feesAmount);

        if (currentBalance.compareTo(transactionAmount) < 0) {
            model.addAttribute("currentRecipient", recipientIdd);
            model.addAttribute("error", true);
            model.addAttribute("message", "Số dư không đủ");
            return "/customer/transfer";

        }
//        BigDecimal newBalance = currentBalance.subtract(transactionAmount);
//        sender.setBalance(newBalance);
//        customerService.save(sender);
//
//        BigDecimal recipientCurrentBalance = recipient.getBalance();
//        BigDecimal recipientNewBalance = recipientCurrentBalance.add(transfer.getTransferAmount());
//        recipient.setBalance(recipientNewBalance);
//        customerService.save(recipient);

        transfer.setId(null);
        transfer.setSender(sender);
        transfer.setRecipient(recipient);
        transfer.setTransferAmount(transferAmount);
        transfer.setFeesAmount(feesAmount);
        transfer.setTransactionAmount(transactionAmount);

        customerService.transfer(transfer);

        model.addAttribute("transfer", transfer);

        model.addAttribute("currentRecipient", recipientId);
        model.addAttribute("success", true);
        model.addAttribute("message", "Chuyển thành công " + transferAmount + " $ vào tài khoản " + recipient.getFullName());

        return "/customer/transfer";


    }

    @GetMapping("/transfer-history")
    public String showHistoryTransferPage(Model model) {
        List<Transfer> transfers = transferService.findAll();

        model.addAttribute("transfers", transfers);

        return "customer/transfer-history";
    }
}
