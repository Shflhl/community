package life.community.controller;

import life.community.dto.QuestionDTO;
import life.community.mapper.UserMapper;
import life.community.model.User;
import life.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Controller
public class IndexController {

    @Autowired(required = false)
    private UserMapper userMapper;

    @Autowired
    private QuestionService questionService;

    @GetMapping
    public String index(
            HttpServletRequest request,
            Model model){
        Cookie[] cookies = request.getCookies();
        if (cookies != null)
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user = userMapper.findByToken(token);
                    if (user != null){
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        // 获取问题列表,并且放入model，名字为question
        List<QuestionDTO> questionDTOList = questionService.list();
        model.addAttribute("questions",questionDTOList);
        return "index";
    }
}
