package life.gutong.ceer.dto;

import lombok.Data;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: QuestionQueryDTO
 * @Description: 查询问题DTO类
 * @Author: ceer
 * @CreateDate: 2019/11/19 22:24
 */
@Data
public class QuestionQueryDTO {
    private String search;  //模糊查询的查询内容
    private Integer page;   //当前页数
    private Integer size;   //每一页的数量
}
