package controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

/*URI Pattern
?  - trùng khớp với 1 ký tự
*  - trùng khớp với 0 hoặc nhiều ký tự
** - trùng khớp với 0 hoặc nhiều phần của đường dẫn (path
segment)
*/
@Controller
public class HelloController {

    @RequestMapping("/car?/s?o?/info1")
    public String info1(Model model) {
        model.addAttribute("message", "Info1");
        return "/index";
    }

    @RequestMapping("/c*/s*d/info2")
    public ModelAndView info2() {
        ModelAndView model = new ModelAndView("/index");
        model.addObject("message", "Info2");
        return model;
    }

    @RequestMapping("/card/**")
    public ModelAndView info3() {
        ModelAndView model = new ModelAndView("/index");
        model.addObject("message", "Info3");
        return model;
    }
}
