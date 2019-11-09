package life.gutong.ceer.mapper;

import life.gutong.ceer.model.User;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Service;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.mapper
 * @ClassName: UserMapper
 * @Description: 用户mapper类
 * @Author: ceer
 * @CreateDate: 2019/11/4 15:57
 */
@Mapper
public interface UserMapper {
  
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified,avatar_url)" +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified},#{avatarUrl})")
    /**
     * @Description: 添加用户信息
     * @return: void
     * @Author: ceer
     * @Date: 2019/11/4
     */
    void insert(User user);


    @Select("select * from user where token = #{token}")
    /**
     * @Description: 根据token查找对应的用户
     * @return:
     * @Author: ceer
     * @Date: 2019/11/4
     */
    User findUserByToken(@Param("token") String token);


    @Select("select * from user where id = #{id}")
    /**
     * @Description: 通过id查找用户
     * @return: void
     * @Author: ceer
     * @Date: 2019/11/6
     */
    User findUserById(@Param("id") Integer id);


    @Select("select * from user where account_id = #{accountId}")
    /**
     * @Description:   通过账户id查出对应的用户
   * @return: life.gutong.ceer.model.User
     * @Author: ceer
     * @Date: 2019/11/8
     */ 
    User findUserByAccountId(@Param(value = "accountId") String accountId);


    @Update("update user set name = #{name},token = #{token},gmt_modified = #{gmtModified},avatar_url = #{avatarUrl} where id = #{id}")
    /**
     * @Description:  根据用户id更改用户信息
     * @return: void
     * @Author: ceer
     * @Date: 2019/11/8
     */
    void updateUserById(User user);
}
