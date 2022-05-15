package tr.edu.duzce.mf.bm.yancim.exception.handler;

import net.sf.json.JSONObject;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.servlet.NoHandlerFoundException;
import tr.edu.duzce.mf.bm.yancim.core.exception.NotAllowedException;
import tr.edu.duzce.mf.bm.yancim.core.exception.WIPMethodException;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorResult;

import javax.servlet.http.HttpServletRequest;

@ControllerAdvice
public class ApplicationExceptionHandler {
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {NullPointerException.class, ArithmeticException.class})
    public @ResponseBody String handleException(HttpServletRequest request, Exception exception) {
        ErrorResult result = new ErrorResult(request.getRequestURI() + " isteği gerçekleştirilirken bir hata meydana geldi.\t/" + exception.getMessage());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public @ResponseBody String handleDataIntegrityViolationException(HttpServletRequest request, Exception exception) {
        ErrorResult result = new ErrorResult(request.getRequestURI() + " isteği gerçekleştirilirken eşsiz değer bulundurması gereken bir alana şartı karşılamayan bir değer denendi.");
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NoHandlerFoundException.class)
    public String handleNoHandlerFoundException(HttpServletRequest request, Exception exception) {
        System.err.println(String.format("%s isteğine karşılık karşılayıcı bulunamadı.\nHata mesajı: %s", request.getRequestURI(), exception.getMessage()) + exception.getMessage());
        return "404";
    }

    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = {WIPMethodException.class, NotAllowedException.class})
    public @ResponseBody String handleAuthenticationErrors(HttpServletRequest request, Exception exception) {
        ErrorResult result = new ErrorResult(exception.getMessage());
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", result);
        return jsonObject.toString();
    }
}
