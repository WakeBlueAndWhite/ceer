package life.gutong.ceer.mapper;

import life.gutong.ceer.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.mapper
 * @ClassName: QuestionMapper
 * @Description: 发布问题Mapper类
 * @Author: ceer
 * @CreateDate: 2019/11/5 16:52
 */
@Mapper
public interface QuestionMapper {

    @Insert("insert into question (title,description,gmt_create,gmt_modified,creator," +
            "tag) values (#{title},#{description}," +
            "#{gmtCreate},#{gmtModified},#{creator},#{tag})")
    /**
     * @Description： 将用户发布的问题存入question表
     * @return: void
     * @Author: ceer
     * @Date: 2019/11/6
     */
    void create(Question question);


    @Select("select * from question")
    /**
     * @Description: 从数据库中查出所有的question数据
    * @return: java.util.List<life.gutong.ceer.model.Question>
     * @Author: ceer
     * @Date: 2019/11/6
     */
    List<Question> selectAll();


    @Select("select * from question limit #{offset},#{size}")
    /**
     * @Description:  分页
     * @return: java.util.List<life.gutong.ceer.model.Question>
     * @Author: ceer
     * @Date: 2019/11/6
     */ 
    List<Question> showPage(@Param(value = "offset") Integer offset,@Param(value = "size") Integer size);


    @Select("select count(1) from question")
    /**
     * @Description:  查询出表中所有的数据总数
     * @return: java.lang.Integer
     * @Author: ceer
     * @Date: 2019/11/6
     */ 
    Integer listCount();
}
