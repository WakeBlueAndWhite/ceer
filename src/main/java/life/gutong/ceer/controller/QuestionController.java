package life.gutong.ceer.controller;

import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

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

    @GetMapping("/question/{id}")
    public String question(@PathVariable(name = "id")Integer id, Model model){

        QuestionDTO questionDTO = questionService.selectQuestionById(id);
        model.addAttribute("question",questionDTO);
        return "question";
    }
}
