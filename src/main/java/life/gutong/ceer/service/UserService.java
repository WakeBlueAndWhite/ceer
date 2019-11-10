package life.gutong.ceer.service;

import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.User;
import life.gutong.ceer.model.UserExample;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

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
        //根据唯一的账号id(AccountId)获取用户
        UserExample userExample = new UserExample();
        userExample.createCriteria().andAccountIdEqualTo(user.getAccountId());
        List<User> users = userMapper.selectByExample(userExample);
        if (users.size() == 0){
            //如果没有获取到  表示此用户为第一次登录  将次用户存入数据库
            user.setGmtCreate(System.currentTimeMillis());
            user.setGmtModified(user.getGmtCreate());
            userMapper.insert(user);
        }else {
            //如果获取到了 则将用户的信息更新到数据库
            User dbUser = users.get(0);
            User updateUser = new User();
            updateUser.setGmtModified(System.currentTimeMillis());
            updateUser.setAvatarUrl(user.getAvatarUrl());
            updateUser.setName(user.getName());
            updateUser.setToken(user.getToken());
            //设置通过id查询的条件
            UserExample userExample1 = new UserExample();
            userExample1.createCriteria().andIdEqualTo(dbUser.getId());
            /**
             * 第一个参数 是要修改的部分值组成的对象，其中有些属性为null则表示该项不修改。
             * 第二个参数 是一个对应的查询条件的类， 通过这个类可以实现 order by 和一部分的where 条件。
             */
            userMapper.updateByExampleSelective(updateUser,userExample1);
        }

    }
}
