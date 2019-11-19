package life.gutong.ceer.controller;

import life.gutong.ceer.cache.TagCache;
import life.gutong.ceer.dto.QuestionDTO;
import life.gutong.ceer.model.Question;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: PublishController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/5 15:00
 */
@Controller
public class PublishController {

    @Autowired
    private QuestionService questionService;


    @GetMapping("/publish/{id}")
    public String poPublish(@PathVariable(name = "id")Long id,Model model){
        //通过id查出对应的question 并将对应的内容回显于前端页面
        QuestionDTO question = questionService.selectQuestionById(id);
        model.addAttribute("title",question.getTitle());
        model.addAttribute("description",question.getDescription());
        model.addAttribute("tag",question.getTag());
        //将id回显于前端的隐藏域中  随着表单的提交而获取id的值
        model.addAttribute("id",question.getId());
        model.addAttribute("tags", TagCache.getAllTags());
        return "publish";
    }

    @GetMapping("/publish")
    public String publish(Model model){
        model.addAttribute("tags", TagCache.getAllTags());
        return "publish";
    }

    @PostMapping("/publish")
    public String doPublish(
            @RequestParam(value = "title",required = false)String title,
            @RequestParam(value = "description",required = false)String description,
            @RequestParam(value = "tag",required = false)String tag,
            @RequestParam(value = "id",required = false)Long id,
            HttpServletRequest request,
            Model model){
        //将用户填写的字段回显在页面
        model.addAttribute("title",title);
        model.addAttribute("description",description);
        model.addAttribute("tag",tag);
        model.addAttribute("tags", TagCache.getAllTags());
        //如果用户没有填写相对应得内容 则将对应得信息回显与页面
        if (title == null || "".equals(title)){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }
        if (description == null ||  "".equals(description)){
            model.addAttribute("error","问题补充不能为空");
            return "publish";
        }
        if (tag == null || "".equals(tag)){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }

        String invalid = TagCache.filterInvalid(tag);
        if (StringUtils.isNotBlank(invalid)) {
            model.addAttribute("error", "输入非法标签:" + invalid);
            return "publish";
        }
        //从session中获取user
        User user = (User) request.getSession().getAttribute("user");
        //如果用户为登录  向前端页面返回错误信息
        if (user == null){
            model.addAttribute("error","用户未登录");
            return "publish";
        }
        //获取Question实体对象
        Question question = new Question();
        question.setTitle(title);
        question.setDescription(description);
        question.setTag(tag);
        question.setCreator(user.getId());
        question.setGmtCreate(System.currentTimeMillis());
        question.setGmtModified(question.getGmtCreate());
        question.setId(id);
        //如果是更改问题 则获取用户id以及更改内容
        // 如果是新问题 则往数据库里存入用户发出的问题
        questionService.createOrUpdateQuestion(question);
        return "redirect:/";
    }
}
