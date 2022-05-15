package tr.edu.duzce.mf.bm.yancim.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StreamUtils;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.writer.ResponseWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Collection;
import java.util.Collections;

public class LoggingInterceptor implements HandlerInterceptor {

    public static Logger logger = LoggerFactory.getLogger(LoggingInterceptor.class);


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (!request.getParameterMap().isEmpty())
            for (String parameter : Collections.list(request.getParameterNames())) {
                String[] values = request.getParameterValues(parameter);
                for (int index = 0; index < values.length; index++) {
                    logger.info("{} parametresinin {}. değeri: {}", parameter, index, values[index]);
                }
            }

        InputStream requestBody = request.getInputStream();
        byte[] byteContent = StreamUtils.copyToByteArray(requestBody);
        String content = new String(byteContent, StandardCharsets.UTF_8);
        if (content.length() != 0)
            logger.info("İstek gövdesi: {}", content);


        return HandlerInterceptor.super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        logger.info(ResponseWriter.getContent(response));
        HandlerInterceptor.super.postHandle(request, response, handler, modelAndView);
    }

}