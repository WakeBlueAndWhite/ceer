package life.gutong.ceer.service;

import life.gutong.ceer.dto.PaginationDTO;
import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.dto.QuestionQueryDTO;
import life.gutong.ceer.exception.CustomizeException;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import life.gutong.ceer.mapper.QuestionExtMapper;
import life.gutong.ceer.mapper.QuestionMapper;
import life.gutong.ceer.mapper.UserMapper;
import life.gutong.ceer.model.Question;
import life.gutong.ceer.model.QuestionExample;
import life.gutong.ceer.model.User;
import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.session.RowBounds;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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
    @Autowired
    private QuestionExtMapper questionExtMapper;
    /**
     * @Description:   首页问题分页显示
     * @return: 
     * @Author: ceer
     * @Date: 2019/11/12
     */ 
    public PaginationDTO list(String search,Integer page,Integer size){
        String searchs = null;
        if (StringUtils.isNotBlank(search)){
            searchs = StringUtils.replace(search, " ","|");
        }
        PaginationDTO paginationDTO = new PaginationDTO();
        Integer totalPage;
        QuestionQueryDTO questionQueryDTO = new QuestionQueryDTO();
        questionQueryDTO.setSearch(searchs);
        Integer totalCount = questionExtMapper.selectCountBySearch(questionQueryDTO);
        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (totalCount % size == 0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }

        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        /**page size*(page-1)
         * 1    5
         * 2    10
         * 3    15
         */
        //每页显示的数据数量
        Integer offset = size * (page-1);
        questionQueryDTO.setSize(size);
        questionQueryDTO.setPage(offset);
        //从数据库中分页查询questionList
        List<Question> questionList = questionExtMapper.selectQuestionBySearch(questionQueryDTO);
        //声明questionDTOList
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question  question: questionList) {
            //通过循环查出用户   Question中的creator是User中的id
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象复制给questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            //保存用户
            questionDTO.setUser(user);
            //将questionDTO对象放入集合中
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }
    /**
     * @Description:   通过用户id 查出用户的问题 并分页显示于用户问题列表中
     * @return: 
     * @Author: ceer
     * @Date: 2019/11/12
     */ 
    public PaginationDTO showQuestionById(Long userId, Integer page, Integer size) {
        PaginationDTO paginationDTO = new PaginationDTO();
        //根据用户id查出对应的问题列表的数量
        Integer totalPage;
        QuestionExample questionExample = new QuestionExample();
        questionExample.createCriteria().andCreatorEqualTo(userId);
        Integer totalCount = (int)questionMapper.countByExample(questionExample);
        //分页的总页数 如果数据库总数量%每页显示的数量等于0
        // 表示可以整除 即当前分页数量为前者除以后者 否则+1
        if (totalCount % size == 0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }

        //校验分页查询语句  如果page当前页小于1 page=1 如果大于分页总页数 page=总页数
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        paginationDTO.setPagination(totalPage,page);
        /**page size*(page-1)
         * 1    5
         * 2    10
         * 3    15
         */
        //每页显示的数据数量
        Integer offset = size * (page-1);
        //根据用户ID从数据库中分页查询questionList
        QuestionExample questionExample1 = new QuestionExample();
        questionExample1.createCriteria().andCreatorEqualTo(userId);
        List<Question> questionList  = questionMapper.selectByExampleWithRowbounds(
                questionExample1,new RowBounds(offset,size));
        //声明questionDTOList
        List<QuestionDTO> questionDTOList = new ArrayList<>();
        for (Question  question: questionList) {
            //通过循环查出用户   Question中的creator是User中的id
            User user = userMapper.selectByPrimaryKey(question.getCreator());
            QuestionDTO questionDTO = new QuestionDTO();
            //将question对象复制给questionDTO
            BeanUtils.copyProperties(question,questionDTO);
            //保存用户
            questionDTO.setUser(user);
            //将questionDTO对象放入集合中
            questionDTOList.add(questionDTO);
        }
        paginationDTO.setData(questionDTOList);
        return paginationDTO;
    }

    public QuestionDTO selectQuestionById(Long id) {

        Question question = questionMapper.selectByPrimaryKey(id);
        //如果question为空 则抛出自定义异常
        if (question == null){
            throw new CustomizeException(CustomizeStatusMessage.QUESTION_NOT_FOUNT);
        }
        QuestionDTO questionDTO = new QuestionDTO();
        //将question对象复制给questionDTO
        BeanUtils.copyProperties(question,questionDTO);
        User user = userMapper.selectByPrimaryKey(question.getCreator());
        questionDTO.setUser(user);
        return questionDTO;
    }
    /**
     * @Description:  根据从数据库中是否获取到id来判断创建question或者更新question
     * @return: 
     * @Author: ceer
     * @Date: 2019/11/12
     */ 
    public void createOrUpdateQuestion(Question question) {
        //如果获取到的id为空 则表示是新插入的问题 将问题存入数据库
        if (question.getId() == null){
            question.setGmtCreate(System.currentTimeMillis());
            question.setGmtModified(question.getGmtCreate());
            //第一次存问题时 将参数设置为0
            question.setViewCount(0);
            question.setLikeCount(0);
            question.setCommentCount(0);
            questionMapper.insert(question);
        }else{
            //如果不是 则表示是编辑更新后的问题 更新时间 并将更新过的内容插入数据库
            Question updateQuestion = new Question();
            updateQuestion.setGmtModified(question.getGmtModified());
            updateQuestion.setTitle(question.getTitle());
            updateQuestion.setDescription(question.getDescription());
            updateQuestion.setTag(question.getTag());
            //设置通过id查询的条件
            QuestionExample questionExample = new QuestionExample();
            questionExample.createCriteria().andIdEqualTo(question.getId());
            /**
             * 第一个参数 是要修改的部分值组成的对象，其中有些属性为null则表示该项不修改。
             * 第二个参数 是一个对应的查询条件的类， 通过这个类可以实现 order by 和一部分的where 条件。
             */
            int i = questionMapper.updateByExampleSelective(updateQuestion, questionExample);
            //如果没有更改成功 则抛出自定义异常
            if (i != 1){
                throw new CustomizeException(CustomizeStatusMessage.QUESTION_NOT_FOUNT);
            }
        }
    }
    
    /**
     * @Description:  增加点赞数 每浏览一次加1
     * @return: 
     * @Author: ceer
     * @Date: 2019/11/12
     */ 
    public void addViewCount(Long id) {
        Question question = new Question();
        question.setId(id);
        question.setViewCount(1);
        questionExtMapper.addViewCount(question);
    }

    public List<QuestionDTO> selectRelated(QuestionDTO queryDTO) {
        if (StringUtils.isBlank(queryDTO.getTag())){
            return new ArrayList<>();
        }
        //将标签中的 , 换成 | 用于sql的正则表达式
        //例java,spring,springboot -> java|spring|springboot
        String tags = StringUtils.replace(queryDTO.getTag(), ",", "|");
        Question question = new Question();
        question.setTag(tags);
        question.setId(queryDTO.getId());

        //模糊查询出所有标签相关的question
        List<Question> questions = questionExtMapper.selectRelated(question);

        List<QuestionDTO> questionDTOS = questions.stream().map(q -> {
            QuestionDTO questionDTO = new QuestionDTO();
            BeanUtils.copyProperties(q, questionDTO);
            return questionDTO;
        }).collect(Collectors.toList());

        return questionDTOS;
    }
}
