package life.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class GreetingController {

    @GetMapping("/greeting") //@RequestParam中的name是表示将要查询一个名字为“name”的参数，并且把值放到后面跟着的参数中去
    public String greeting(@RequestParam(name = "name",required = false,defaultValue = "World!")
                           String name, Model model){
        model.addAttribute("name",name);
        return "index";
    }
}
