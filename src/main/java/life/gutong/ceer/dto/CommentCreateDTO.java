package life.gutong.ceer.dto;

import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: CommentCreateDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/11 17:17
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}
