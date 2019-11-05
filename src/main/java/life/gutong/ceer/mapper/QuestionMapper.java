package life.gutong.ceer.mapper;

import life.gutong.ceer.model.Question;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;

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
    void create(Question question);
}
