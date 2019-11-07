package life.gutong.ceer.dto;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.dto
 * @ClassName: PaginationDTO
 * @Description: 分页实体类传输对象
 * @Author: ceer
 * @CreateDate: 2019/11/6 22:01
 */
@Data
public class PaginationDTO {
    private List<QuestionDTO> questions;
    private Boolean showPrevious;
    private Boolean showFirstPage;
    private Boolean showNext;
    private Boolean showEndPage;
    private Integer page;
    private List<Integer> pages = new ArrayList<>();
    private Integer totalPage;
    public void setPagination(Integer totalCount, Integer page, Integer size) {
        //分页的总页数 如果数据库总数量%每页显示的数量等于0
        // 表示可以整除 即当前分页数量为前者除以后者 否则+1
        if (totalCount % size == 0){
            totalPage = totalCount/size;
        }else{
            totalPage = totalCount/size + 1;
        }
        if (page < 1){
            page = 1;
        }
        if (page > totalPage){
            page = totalPage;
        }
        this.page = page;
        pages.add(page);
        for (int i = 1; i <=3 ; i++) {
            if (page-i>0){
                pages.add(0,page-i);
            }
            if (page+i<= totalPage){
                pages.add(page+i);
            }
        }
        //是否展示上一页
        if (page==1){
            showPrevious = false;
        }else {
            showPrevious = true;
        }

        //是否展示下一页
        if (page == totalPage){
            showNext = false;
        }else{
            showNext = true;
        }

        //是否展示第一页
        if (pages.contains(1)){
            showFirstPage = false;
        }else {
            showFirstPage = true;
        }

        //是否展示最后一页
        if (pages.contains(totalPage)){
            showEndPage = false;
        }else{
            showEndPage = true;
        }

    }
}
