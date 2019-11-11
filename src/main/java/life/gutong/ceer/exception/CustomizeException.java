package life.gutong.ceer.exception;

/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.exception
 * @ClassName: CustomizeException
 * @Description: java类作用描述
 * @Author: ceer
 * @CreateDate: 2019/11/10 19:28
 */
public class CustomizeException extends RuntimeException{

    private String message;
    private CustomizeStatusMessage customizeStatusMessage;

    public CustomizeException(String message) {
        this.message = message;
    }

    public CustomizeException(CustomizeStatusMessage customizeStatusMessage) {
        this.customizeStatusMessage = customizeStatusMessage;
        this.message = customizeStatusMessage.getMessage();
    }

    public CustomizeStatusMessage getCustomizeStatusMessage() {
        return customizeStatusMessage;
    }

    public void setCustomizeStatusMessage(CustomizeStatusMessage customizeStatusMessage) {
        this.customizeStatusMessage = customizeStatusMessage;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
