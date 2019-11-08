package life.gutong.ceer.controller;

import life.gutong.ceer.dto.PaginationDTO;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.QuestionService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: ProfileController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/7 20:20
 */
@Controller
public class ProfileController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action")String action, Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "2") Integer size){
        User user = null;
        //获取存贮的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length !=0) {
            for (Cookie cookie : cookies) {
                //获取token的value值 用来查找用户
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                     user = userMapper.findUserByToken(token);
                    if (user != null) {
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        if (user == null){
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
        }
        if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
        }
        PaginationDTO paginationDTOList = questionService.showQuestionById(user.getId(), page, size);
        model.addAttribute("paginationDTOList",paginationDTOList);
        return "profile";
    }
}
