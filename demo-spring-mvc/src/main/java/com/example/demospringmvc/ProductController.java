package com.example.demospringmvc;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductController {
    @GetMapping("/hello")
    public String xinchao (@RequestParam (value = "name" ,defaultValue = "World!") String str) {
      return "Hello " + str;
//      return "index";
    }

}
