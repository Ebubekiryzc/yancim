package tr.edu.duzce.mf.bm.yancim.core.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import tr.edu.duzce.mf.bm.yancim.core.exception.NotAllowedException;
import tr.edu.duzce.mf.bm.yancim.core.exception.WIPMethodException;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.DenyAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.abstracts.JWTHelper;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.CustomClaim;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;

public class JWTAuthInterceptor implements HandlerInterceptor {

    @Autowired
    private JWTHelper jwtHelper;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HandlerMethod handlerMethod = (HandlerMethod) handler;
        Method method = handlerMethod.getMethod();

        if (method.isAnnotationPresent(PermitAll.class)) {
            return true;
        } else if (method.isAnnotationPresent(RolesAllowed.class)) {

            // RolesAllowed ile işaretlenen yerdeki RolesAllowed içeriğini liste olarak almak:
            List<String> roles = Arrays.asList(method.getAnnotation(RolesAllowed.class).value());

            String auth = request.getHeader(HttpHeaders.AUTHORIZATION);
            if (auth == null)
                throw new NotAllowedException();

            CustomClaim claims = jwtHelper.verify(auth);

            // Buradaki rollerden birisi bile claim'deki rollerle eşleşiyorsa onaylamak, eşleşmiyorsa onaylamamak.
            for (String role : claims.getRoles()) {
                if (roles.contains(role)) return true;
            }

            throw new NotAllowedException();
        } else if (method.isAnnotationPresent(DenyAll.class)) {
            throw new NotAllowedException();
        } else {
            throw new WIPMethodException();
        }
    }
}
