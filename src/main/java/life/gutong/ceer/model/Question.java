package life.gutong.ceer.model;


import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.model
 * @ClassName: Question
 * @Description: 发布问题的实体类
 * @Author: ceer
 * @CreateDate: 2019/11/5 17:06
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
}
