package life.gutong.ceer.service;

import life.gutong.ceer.dto.CommentDTO;
import life.gutong.ceer.enums.CommentTypeEnum;
import life.gutong.ceer.enums.NotificationStatusEnum;
import life.gutong.ceer.enums.NotificationTypeEnum;
import life.gutong.ceer.exception.CustomizeException;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import life.gutong.ceer.mapper.*;
import life.gutong.ceer.model.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.service
 * @ClassName: CommentService
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/11 23:13
 */
@Service
public class CommentService {

    @Autowired
    private CommentMapper commentMapper;

    @Autowired
    private QuestionMapper questionMapper;

    @Autowired
    private QuestionExtMapper questionExtMapper;

    @Autowired
    private UserMapper userMapper;

    @Autowired
    private CommentExtMapper commentExtMapper;

    @Autowired
    private NotificationMapper notificationMapper;
    //事务注解   在问题中 插入问题后要执行增加浏览次数
    // 如果在插入问题后出现出现异常 那么后面的代码就不会执行了
    // 加入注解后 整段代码要么都成功 要么都不成功
    @Transactional
    public void heart(Comment comment, User commentor){
        //数据校验
        if (comment.getParentId() == null || comment.getParentId() == 0){
            throw new CustomizeException(CustomizeStatusMessage.TARGET_PARAM_NOT_FOUND);
        }
        //参数类型校验
        if (comment.getType() == null || !CommentTypeEnum.isExit(comment.getType())){
            throw new CustomizeException(CustomizeStatusMessage.TYPE_PARAM_WRONG);
        }

        //评论/问题校验
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()){
            //回复评论
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeStatusMessage.COMMENT_NOT_FOUND);
            }
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(dbComment.getParentId());
            if (question == null){
                throw  new CustomizeException(CustomizeStatusMessage.QUESTION_NOT_FOUNT);
            }

            commentMapper.insert(comment);
            // 增加评论数
            Comment parentComment = new Comment();
            parentComment.setId(comment.getParentId());
            parentComment.setCommentCount(1);
            commentExtMapper.addCommentCount(parentComment);
            //创建通知
            createNotify(comment,dbComment.getCommentator(),commentor.getName(),question.getTitle(),NotificationTypeEnum.REPLY_COMMENT,question.getId());
        }else {
            //回复问题
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if (question == null){
                throw  new CustomizeException(CustomizeStatusMessage.QUESTION_NOT_FOUNT);
            }
            commentMapper.insert(comment);
            question.setCommentCount(1);
            questionExtMapper.addCommentCount(question);
            //创建通知
            createNotify(comment,question.getCreator(),commentor.getName(),question.getTitle(),NotificationTypeEnum.REPLY_QUESTION,question.getId());

        }
    }
                                                //用户id       //评论人的用户名称    //评论的问题标题
    private void createNotify(Comment comment, Long receiver,String notifierName, String outerTitle, NotificationTypeEnum notificationTypeEnum,Long outerId){
        //当自己回复自己的评论时 return 不创建新的回复通知
        if (comment.getCommentator().equals(receiver)) {
            return;
        }
        Notification notification = new Notification();
        //获取当前时间
        notification.setGmtCreate(System.currentTimeMillis());
        //获取当前类型 回复的是问题还是评论
        notification.setType(notificationTypeEnum.getType());
        //回复：获取当前问题的id
        notification.setOuterid(outerId);
        //获取回复问题人的用户id
        notification.setNotifier(comment.getCommentator());
        //设置当前状态为未读状态
        notification.setStatus(NotificationStatusEnum.UNREAD.getStatus());
        //获取当前的问题的用户id
        notification.setReceiver(receiver);
        //获取当前的评论人的用户名称
        notification.setNotifierName(notifierName);
        //获取当前的评论的问题标题
        notification.setOuterTitle(outerTitle);
        notificationMapper.insert(notification);
    }

    public List<CommentDTO> selectQuestionDTOByTargetId(Long id,CommentTypeEnum commentTypeEnum) {
        CommentExample commentExample = new CommentExample();
        commentExample.createCriteria()
                .andParentIdEqualTo(id)
                .andTypeEqualTo(commentTypeEnum.getType());
        //设置创建时间倒序排序
        commentExample.setOrderByClause("gmt_create desc");
        List<Comment> comments = commentMapper.selectByExample(commentExample);
        if (comments.size() == 0){
            return new ArrayList<>();
        }

        //获取去重的评论人
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        List<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人并转换为Map
        UserExample userExample = new UserExample();
        userExample.createCriteria()
                    .andIdIn(userIds);
        List<User> users = userMapper.selectByExample(userExample);
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));

        //转换comment为 commentDTO
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment, commentDTO);
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());

        return commentDTOS;
    }
}
