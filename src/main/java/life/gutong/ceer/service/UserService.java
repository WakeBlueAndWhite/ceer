package life.gutong.ceer.service;

import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.service
 * @ClassName: UserService
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/8 21:55
 */
@Service
public class UserService {

    @Autowired
    private UserMapper userMapper;

    public void createOrUpdate(User user){
        //根据唯一的账号id获取用户
        User dbUser = userMapper.findUserByAccountId(user.getAccountId());
        if (dbUser == null){
            //如果没有获取到  表示此用户为第一次登录  将次用户存入数据库
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //如果获取到了 则将用户的信息更新到数据库
            dbUser.setGmtModified(System.currentTimeMillis());
            dbUser.setAvatarUrl(user.getAvatarUrl());
            dbUser.setName(user.getName());
            dbUser.setToken(user.getToken());
            userMapper.updateUserById(dbUser);
        }

    }
}
