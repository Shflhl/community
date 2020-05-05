package life.community.controller;

import life.community.dto.PageDTO;
import life.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import javax.servlet.http.HttpServletRequest;

@Controller
public class IndexController {

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public String index(
            HttpServletRequest request,
            Model model,
            @RequestParam(name = "page", defaultValue = "1") Integer page,
            @RequestParam(name = "size", defaultValue = "5") Integer size){
        // 获取问题列表,并且放入model，名字为question
        PageDTO pagination = questionService.list(page,size);
        if (pagination == null)
            return "error";
        model.addAttribute("pagination",pagination);
        return "index";
    }
}
