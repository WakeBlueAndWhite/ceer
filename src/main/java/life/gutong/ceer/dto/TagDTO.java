package life.gutong.ceer.dto;

import lombok.Data;

import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: TagDTO
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/15 19:27
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}
