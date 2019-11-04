package life.gutong.ceer.mapper;

import life.gutong.ceer.model.User;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

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
  
    @Insert("insert into user (name,account_id,token,gmt_create,gmt_modified)" +
            "values (#{name},#{accountId},#{token},#{gmtCreate},#{gmtModified})")
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
}
