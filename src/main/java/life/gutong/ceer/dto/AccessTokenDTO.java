package life.gutong.ceer.dto;

import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: AccessTokenDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/3 20:06
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;
}
