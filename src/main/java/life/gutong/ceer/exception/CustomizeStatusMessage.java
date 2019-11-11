package life.gutong.ceer.exception;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.exception
 * @ClassName: CustomizeStatusMessage
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/10 19:32
 */
public enum CustomizeStatusMessage {
    QUESTION_NOT_FOUNT("你找的问题不存在，要不换个问题试试！！！");;

    private String message;

    CustomizeStatusMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
