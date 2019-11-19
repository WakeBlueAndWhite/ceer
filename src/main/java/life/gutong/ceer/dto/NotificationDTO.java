package life.gutong.ceer.dto;


import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: NotificationDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/16 19:49
 */
@Data
public class NotificationDTO {
    private Long id;            //通知的主键id
    private Long gmtCreate;     //创建时间
    private Integer status;     //状态  已读 未读
    private Long notifier;      //评论人
    private String notifierName;//评论人的用户名称
    private String outerTitle; //被评论或者回复评论的问题的标题
    private Long outerid;     //当前问题的id
    private Integer type;      //类型 1 问题 2评论
    private String typeName; //类型 问题 还是 评论
}
