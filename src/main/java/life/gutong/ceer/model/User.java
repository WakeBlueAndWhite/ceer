package life.gutong.ceer.model;


import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.model
 * @ClassName: User
 * @Description: 用户实体类
 * @Author: ceer
 * @CreateDate: 2019/11/4 16:03
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}
