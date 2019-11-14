package life.gutong.ceer.advice;

import com.alibaba.fastjson.JSON;
import life.gutong.ceer.dto.ResultDTO;
import life.gutong.ceer.exception.CustomizeException;
import life.gutong.ceer.exception.CustomizeStatusMessage;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;


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
    public ModelAndView handle(Throwable e, Model model, HttpServletRequest request, HttpServletResponse response) {
        String contentType = request.getContentType();
        if ("application/json".equals(contentType)) {
            ResultDTO resultDTO;
            if (e instanceof CustomizeException) {
                resultDTO = ResultDTO.errorOf((CustomizeException) e);
            } else {
                resultDTO = ResultDTO.errorOf(CustomizeStatusMessage.SYS_ERROR);
            }
            try {
                response.setContentType("application/json");
                response.setStatus(200);
                response.setCharacterEncoding("utf-8");
                PrintWriter writer = response.getWriter();
                writer.write(JSON.toJSONString(resultDTO));
                writer.close();
            } catch (IOException ex) {
                ex.printStackTrace();
            }
            return null;
        } else {
            if(e instanceof CustomizeException) {
                model.addAttribute("message", e.getMessage());
            }else{
                model.addAttribute("message", CustomizeStatusMessage.SYS_ERROR.getMessage());
            }
            return new ModelAndView("error");
        }
    }
}
