package life.gutong.ceer.dto;

import life.gutong.ceer.model.User;
import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: CommentDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/13 0:20
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private User user;
    private Integer commentCount;
}
