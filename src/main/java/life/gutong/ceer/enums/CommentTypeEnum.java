package life.gutong.ceer.enums;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.enums
 * @ClassName: CommentTypeEnum
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/12 0:04
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2),
    ;
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    public static boolean isExit(Integer type){
        for (CommentTypeEnum commentTypeEnum : CommentTypeEnum.values()){
            if (commentTypeEnum.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }
}
