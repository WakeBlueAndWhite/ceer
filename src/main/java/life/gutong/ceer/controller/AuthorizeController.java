package life.gutong.ceer.controller;

import life.gutong.ceer.dto.AccessTokenDTO;
import life.gutong.ceer.dto.GithubUser;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.provider.GithubProvider;
import life.gutong.ceer.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.UUID;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: AuthorizeController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 19:57
 */
@Controller
@Slf4j
public class AuthorizeController {

    @Autowired
    private GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;
    @Value("${github.client_secret}")
    private String clientSecret;
    @Value("${github.redirect_uri}")
    private String redirectUri;

    @Autowired
    private UserService userService;

    @Autowired
    private UserMapper userMapper;
    @GetMapping("callback")
    public String callback(@RequestParam(name = "code")String code,
                           @RequestParam(name = "state")String state,
                           HttpServletRequest request,
                           HttpServletResponse response){
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setRedirect_uri(redirectUri);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        GithubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser!=null && githubUser.getId() != null){
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            //获取用户名称
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));
            //获取用户头像
            user.setAvatarUrl(githubUser.getAvatarUrl());
            //如果用户第一次登录则将数据存入数据库
            //如果不是第一次登录则更新用户数据状态
            userService.createOrUpdate(user);
            response.addCookie(new Cookie("token",token));
            return "redirect:/";
        }else {
            //记录登录github失败日志
            log.error("callback get github error,{}",githubUser);
            return "redirect:/";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request,HttpServletResponse response){
        //清除session
        HttpSession session = request.getSession();
        session.invalidate();
        //清除cookie
        Cookie cookie = new Cookie("token",null);
        cookie.setMaxAge(0);
        cookie.setPath("/");
        response.addCookie(cookie);
        //重定向到index
        return "redirect:/";
    }
}
