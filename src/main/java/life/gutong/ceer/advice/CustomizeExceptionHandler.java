package life.gutong.ceer.advice;

import life.gutong.ceer.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;


/**
 * @ProjectName: ceer
 * @Package: life.gutong.ceer.advice
 * @ClassName: CustomizeExceptionHandler
 * @Description: 全局异常处理
 * @Author: ceer
 * @CreateDate: 2019/11/10 19:18
 */
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    public ModelAndView handle(Throwable e, Model model) {
        if (e instanceof CustomizeException){
            model.addAttribute("message",e.getMessage());
        }else {
            model.addAttribute("message","服务器冒烟了，请稍后再试！！！");
        }
        return new ModelAndView("error");
    }

}
