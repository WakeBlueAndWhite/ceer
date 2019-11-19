package life.gutong.ceer.enums;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.enums
 * @ClassName: NotificationTypeEnum
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/16 14:48
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论")
    ;

    private int type;
    private String message;

    NotificationTypeEnum(int type, String message) {
        this.type = type;
        this.message = message;
    }

    //通过type 获取message
    public static String getMessageByType(int type){
        for (NotificationTypeEnum notificationTypeEnum:NotificationTypeEnum.values()){
            if (notificationTypeEnum.getType() == type){
                return notificationTypeEnum.getMessage();
            }
        }
        return "";
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
