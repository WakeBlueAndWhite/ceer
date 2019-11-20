package life.gutong.ceer.mapper;


import life.gutong.ceer.dto.QuestionQueryDTO;
import life.gutong.ceer.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    //查询出文章查看次数
    int addViewCount(Question question);
    //查询出问题评论次数
    int addCommentCount(Question question);

    List<Question> selectRelated(Question question);
    //查询出问题数量  如果有search的话 查询搜索的标题的数量
    Integer selectCountBySearch(QuestionQueryDTO questionQueryDTO);
    //查询出问题列表  如果有search的话 查询搜索的问题的列表
    List<Question> selectQuestionBySearch(QuestionQueryDTO questionQueryDTO);
}