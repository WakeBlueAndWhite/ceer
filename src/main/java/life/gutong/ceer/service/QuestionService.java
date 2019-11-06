package life.gutong.ceer.service;

import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.mapper.QuestionMapper;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.Question;
import life.gutong.ceer.model.User;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.service
 * @ClassName: QuestionService
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/6 16:06
 */
@Service
public class QuestionService {

    @Resource
    private QuestionMapper questionMapper;
    @Resource
    private UserMapper userMapper;

    public List<QuestionDTO> list(){
        //从数据库中查出所有的questionList
        List<Question> questionList = questionMapper.selectAll();
        //声明questionDTOList
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question  question: questionList) {
            //通过循环查出用户   Question中的creator是User中的id
            User user = userMapper.findUserById(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象复制给questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            //保存用户
            questionDTO.setUser(user);
            //将questionDTO对象放入集合中
            questionDTOList.add(questionDTO);
        }
        return questionDTOList;
    }
}
