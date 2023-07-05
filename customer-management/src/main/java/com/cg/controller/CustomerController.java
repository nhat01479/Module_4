package com.cg.controller;

import com.cg.model.Customer;
import com.cg.service.customer.CustomerServiceImpl;
import com.cg.service.customer.ICustomerService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Controller
@RequestMapping("/customers")
public class CustomerController {

    @GetMapping
//    Có thể return String hoặc ModelAndView đều được
//    public String showListPage() {
//        return "customer/list";
//    }
//    Không đặt đường dẫn GetMapping để mặc định hiển thị danh sách customers
    public ModelAndView showListPage() {
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("customer/list");
        ICustomerService customerService = new CustomerServiceImpl();

        List<Customer> customers = customerService.getAll();

        modelAndView.addObject("customers", customers);

        return modelAndView;
    }


//    Hiển thị trang tạo customer, tạo customer null truyền qua trang create
    @GetMapping("/create")
    public String showCreatePage(Model model) {
        Customer customer = new Customer();
        model.addAttribute("customer", customer);

        return "customer/create";
    }

//    Tạo customer và add vào danh sách, sau đó hiển thị lại trang tạo customer
    @PostMapping("/create")
    public String createCustomer(@ModelAttribute Customer customer, Model model) {
//        @ModelAttribute Customer customer: được truyền qua ở trên
        ICustomerService customerService = new CustomerServiceImpl();
        //"add" của đối tượng customerService sẽ thêm khách hàng mới trực tiếp vào danh sách khách hàng trong lớp CustomerServiceImpl
        customerService.add(customer);
        model.addAttribute("customer", new Customer());
        return "customer/create";
        /*
        Ở đây sẽ add customer vào danh sách customers, không ảnh hưởng tới danh sách trong lớp
        List<Customer> customers = customerService.getAll();
        customers.add(customer);
        */
    }

//    Hiển thị trang info, @PathVariable để đánh dấu biến đường dẫn.
//    Ví dụ:  HTTP có URL là "/customers/123" thì giá trị của biến id là 123
    @GetMapping("/info/{id}")
    public String showInfoPage(@PathVariable Long id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();

        Customer customer = customerService.getById(id);

        model.addAttribute("customer", customer);

        return "customer/info";
    }

//    Hiển thị trang info, đi liền với /ìnfo?id=${id}
    @GetMapping("/info")
    public String showInfoByParamPage(@RequestParam Long id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();

        Customer customer = customerService.getById(id);

        model.addAttribute("customer", customer);

        return "customer/info";
    }

    @GetMapping("/{id}")
    public String showEditPage(@PathVariable String id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();
        try {
            Long customerId = Long.parseLong(id);
            Customer customer = customerService.getById(customerId);

            model.addAttribute("customer", customer);

            return "customer/edit";

        } catch (Exception e) {
            return "error/404";
        }
    }
    @PostMapping("/{id}")
    public String updateCustomer(@PathVariable Long id, @ModelAttribute Customer customer, Model model) {

        ICustomerService customerService = new CustomerServiceImpl();
        customer.setId(id); //customer đươc truyền qua ở hàm showEditPage()
        customerService.update(customer);

        List<Customer> customers = customerService.getAll();

        model.addAttribute("customers", customers);

        return "customer/list";
    }

    @GetMapping("/delete/{id}")
    public String deleteCustomer(@PathVariable String id, Model model) {
        ICustomerService customerService = new CustomerServiceImpl();
        List<Customer> customers = customerService.getAll();

        try {
            Long customerId = Long.parseLong(id);
            Customer customer = customerService.getById(customerId);
            if (customer != null) {
                customers.remove(customer);
            }
            model.addAttribute("customers", customers);

            return "redirect:/customers";

        } catch (Exception e) {
            return "error/404";
        }
    }
}
