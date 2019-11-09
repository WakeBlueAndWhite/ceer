package life.gutong.ceer.controller;

import life.gutong.ceer.dto.PaginationDTO;
import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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

    @Autowired
    private UserMapper userMapper;
    @Resource
    private QuestionService questionService;

    @GetMapping("/")
    public String index(HttpServletRequest request,
            Model model,
    //page为当前页数 默认为第一页
     //size为分页每页显示的数量 默认为5页
    @RequestParam(name = "page",defaultValue = "1") Integer page,
    @RequestParam(name = "size",defaultValue = "3") Integer size){

        //查出所有的QuestionDTOList 并存入model中用于回显与前端页面
        PaginationDTO paginationDTOList = questionService.list(page,size);
        model.addAttribute("paginationDTOList",paginationDTOList);
        return "index";
    }
}
