package life.gutong.ceer.dto;

import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: GithubUser
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 21:33
 */
@Data
public class GithubUser {
    private String name;
    private Long id;
    private String bio;
    private String avatarUrl;
}
