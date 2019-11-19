package life.gutong.ceer.dto;

import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: FileDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/17 21:59
 */
@Data
public class FileDTO {
    private int success;     // 0 表示上传失败，1 表示上传成功
    private String message;  //提示的信息，上传成功或上传失败及错误信息等
    private String url;    // 上传成功时才返回
}
