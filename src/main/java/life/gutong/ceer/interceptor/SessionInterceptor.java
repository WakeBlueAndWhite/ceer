package life.gutong.ceer.interceptor;

import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.model.UserExample;
import life.gutong.ceer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.interceptor
 * @ClassName: SessionInterceptor
 * @Description: 拦截器
 * @Author: ceer
 * @CreateDate: 2019/11/8 15:22
 */
@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null && cookies.length != 0) {
            for (Cookie cookie : cookies) {
                //获取token的value值 用来查找用户
                if ("token".equals(cookie.getName())) {
                    String token = cookie.getValue();
                    UserExample userExample = new UserExample();
                    userExample.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(userExample);
                    if (users.size() != 0) {
                        request.getSession().setAttribute("user", users.get(0));
                        //通过用户id获取回复的信息数量并存入session中
                        Long unReadCount = notificationService.getUnReadCount(users.get(0).getId());
                        request.getSession().setAttribute("unReadCount", unReadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
