package life.gutong.ceer.controller;

import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.mapper.QuestionMapper;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: TestController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 23:28
 */
@Controller
public class IndexController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model){
        //获取存贮的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length !=0) {
            for (Cookie cookie : cookies) {
                //获取token的value值 用来查找用户
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    User user = userMapper.findUserByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //查出所有的QuestionDTOList 并存入model中用于回显与前端页面
        List<QuestionDTO> questionDTOList = questionService.list();
        for (QuestionDTO questionDTO:questionDTOList) {
            questionDTO.setDescription("ceer");
        }
        model.addAttribute("questions",questionDTOList);
        return "index";
    }
}
