package life.gutong.ceer.enums;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.enums
 * @ClassName: NotificationStatusEnum
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/16 14:51
 */
public enum NotificationStatusEnum {
    UNREAD(0),
    READ(1)
    ;
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status =status;
    }
}
