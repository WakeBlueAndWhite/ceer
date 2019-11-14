package life.gutong.ceer.dto;

import life.gutong.ceer.model.User;
import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: QuestionDTO
 * @Description: 问题实体类传输对象
 * @Author: ceer
 * @CreateDate: 2019/11/6 16:05
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    private User user;
}
