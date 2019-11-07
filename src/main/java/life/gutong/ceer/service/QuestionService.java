package life.gutong.ceer.service;

import life.gutong.ceer.dto.PaginationDTO;
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

    public PaginationDTO list(Integer page,Integer size){
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalCount = questionMapper.listCount();
        paginationDTO.setPagination(totalCount,page,size);
        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (page < 1){
            page = 1;
        }
        if (page > paginationDTO.getTotalPage()){
            page = paginationDTO.getTotalPage();
        }
        /**page size*(page-1)
         * 1    5
         * 2    10
         * 3    15
         */
        //每页显示的数据数量
        Integer offset = size * (page-1);
        //从数据库中分页查询questionList
        List<Question> questionList = questionMapper.showPage(offset,size);
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
        paginationDTO.setQuestions(questionDTOList);
        return paginationDTO;
    }
}
