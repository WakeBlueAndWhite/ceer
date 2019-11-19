package life.gutong.ceer.controller;

import life.gutong.ceer.dto.CommentCreateDTO;
import life.gutong.ceer.dto.CommentDTO;
import life.gutong.ceer.dto.ResultDTO;
import life.gutong.ceer.enums.CommentTypeEnum;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import life.gutong.ceer.mapper.CommentMapper;
import life.gutong.ceer.model.Comment;
import life.gutong.ceer.model.User;
import life.gutong.ceer.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.controller
 * @ClassName: CommentController
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/11 17:22
 */
@Controller
public class CommentController {


    @Autowired
    private CommentService commentService;

    @RequestMapping(value = "/comment", method = RequestMethod.POST)
    @ResponseBody
    public Object post(@RequestBody CommentCreateDTO commentDTO, HttpServletRequest request) {
        //验证是否登录
        User user = (User) request.getSession().getAttribute("user");
        if (user == null) {
            return ResultDTO.errorOf(CustomizeStatusMessage.NO_LOGIN);
        }
        //验证是否评论
        if (commentDTO == null || StringUtils.isBlank(commentDTO.getContent())) {
            return ResultDTO.errorOf(CustomizeStatusMessage.CONTENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        comment.setLikeCount(0L);
        comment.setCommentCount(0);
        commentService.heart(comment,user);
        return ResultDTO.okOf();
    }

    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    @ResponseBody
    public ResultDTO<List<CommentDTO>> comments(@PathVariable(name = "id")Long id){
        List<CommentDTO> commentDTOS = commentService.selectQuestionDTOByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}
