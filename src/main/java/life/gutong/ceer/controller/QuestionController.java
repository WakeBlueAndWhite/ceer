package life.gutong.ceer.controller;

import life.gutong.ceer.dto.CommentDTO;
import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.enums.CommentTypeEnum;
import life.gutong.ceer.service.CommentService;
import life.gutong.ceer.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: QuestionController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/8 19:38
 */
@Controller
public class QuestionController {

    @Autowired
    private QuestionService questionService;

    @Autowired
    private CommentService commentService;
    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Long id, Model model){

        QuestionDTO questionDTO = questionService.selectQuestionById(id);
        //模糊查询出与问题标签相关的问题列表
        List<QuestionDTO> relatedQuestions = questionService.selectRelated(questionDTO);
        //查询出问题
        List<CommentDTO> commentDTO = commentService.selectQuestionDTOByTargetId(id,CommentTypeEnum.QUESTION);
        //累计增加阅读数
        questionService.addViewCount(id);
        model.addAttribute("question",questionDTO);
        model.addAttribute("commentDTO",commentDTO);
        model.addAttribute("relatedQuestions",relatedQuestions);
        return "question";
    }
}
