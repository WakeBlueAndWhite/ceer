package life.gutong.ceer.controller;

import life.gutong.ceer.mapper.QuestionMapper;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.Question;
import life.gutong.ceer.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: PublishController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/5 15:00
 */
@Controller
public class PublishController {

    @Resource
    private UserMapper userMapper;

    @Resource
    private QuestionMapper questionMapper;

    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam("title")String title,
            @RequestParam("description")String description,
            @RequestParam("tag")String tag,
            HttpServletRequest request,
            Model model){
        //将用户填写的字段回显在页面
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        //如果用户没有填写相对应得内容 则将对应得信息回显与页面
        if (title == null || "".equals(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null ||  "".equals(description)){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }
        User user = null;
        //获取存贮的cookie
        Cookie[] cookies = request.getCookies();
        if (cookies!=null && cookies.length !=0){
            for (Cookie cookie : cookies) {
            //获取token的value值 用来查找用户
                if ("token".equals(cookie.getName())){
                    String token = cookie.getValue();
                    user = userMapper.findUserByToken(token);
                    if (user!=null){
                         request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }
        //如果用户为登录  向前端页面返回错误信息
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //获取Question实体对象
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        //往数据库里存入用户发出的问题
        questionMapper.create(question);
        return "redirect:/";
    }
}
