package life.gutong.ceer.controller;

import life.gutong.ceer.dto.PaginationDTO;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.NotificationService;
import life.gutong.ceer.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private NotificationService notificationService;

    @Resource
    private QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(HttpServletRequest request,
                          @PathVariable(name = "action")String action, Model model,
                          @RequestParam(name = "page",defaultValue = "1") Integer page,
                          @RequestParam(name = "size",defaultValue = "3") Integer size){

        //从session中获取user
        User user = (User) request.getSession().getAttribute("user");
        if (user == null){
            return "redirect:/";
        }
        if ("questions".equals(action)){
            model.addAttribute("section","questions");
            model.addAttribute("sectionName","我的提问");
            PaginationDTO paginationDTOList = questionService.showQuestionById(user.getId(), page, size);
            model.addAttribute("paginationDTOList",paginationDTOList);
        }
        if ("replies".equals(action)){
            model.addAttribute("section","replies");
            model.addAttribute("sectionName","最新回复");
            //通过用户id获取在问题中回复过的信息详情
            PaginationDTO paginationDTOList = notificationService.list(user.getId(), page, size);
            model.addAttribute("paginationDTOList",paginationDTOList);

        }
        return "profile";
    }
}
