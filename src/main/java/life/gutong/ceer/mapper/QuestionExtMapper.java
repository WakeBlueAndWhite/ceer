package life.gutong.ceer.mapper;


import life.gutong.ceer.model.Question;

import java.util.List;

public interface QuestionExtMapper {
    int addViewCount(Question question);

    int addCommentCount(Question question);

    List<Question> selectRelated(Question question);
}