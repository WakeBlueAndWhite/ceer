package life.gutong.ceer.controller;

import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: TestController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 23:28
 */
@Controller
public class TestController {

    @Resource
    private UserMapper userMapper;

    @GetMapping("/")
    public String getHello(HttpServletRequest request){
        //获取存贮的cookie
        Cookie[] cookies = request.getCookies();
        for (Cookie cookie : cookies) {
                //获取token的value值 用来查找用户
            if ("token".equals(cookie.getName())){
                String token = cookie.getValue();
                User user = userMapper.findUserByToken(token);
                if (user!=null){
                    request.getSession().setAttribute("user",user);
                }
                break;
            }
        }
        return "index";
    }
}
