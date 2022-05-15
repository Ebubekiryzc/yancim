//package tr.edu.duzce.mf.bm.yancim.core.utilities.security.filter;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.JWTVerifier;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.auth0.jwt.interfaces.DecodedJWT;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.authority.SimpleGrantedAuthority;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.web.filter.OncePerRequestFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.HashMap;
//import java.util.Map;
//
//import static java.util.Arrays.stream;
//import static org.springframework.http.HttpHeaders.AUTHORIZATION;
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public class CustomAuthorizationFilter extends OncePerRequestFilter {
//
//    private static Logger logger = LoggerFactory.getLogger(CustomAuthenticationFilter.class);
//
//    @Override
//    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
//        if (httpServletRequest.getServletPath().equals("/auth/login") || httpServletRequest.getServletPath().equals("/users/save_or_update") || httpServletRequest.getServletPath().equals("/index.jsp")) {
//            logger.info("\'{}\' tarafından \'{}\' isteği tetiklendi.", httpServletRequest.getRemoteAddr(), httpServletRequest.getServletPath());
//            filterChain.doFilter(httpServletRequest, httpServletResponse);
//        } else {
//            String authorizationHeader = httpServletRequest.getHeader(AUTHORIZATION);
//            if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
//                try {
//                    String token = authorizationHeader.substring("Bearer ".length());
//                    Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//                    JWTVerifier verifier = JWT.require(algorithm).build();
//                    DecodedJWT decodedJWT = verifier.verify(token);
//                    String username = decodedJWT.getSubject();
//                    String[] roles = decodedJWT.getClaim("roles").asArray(String.class);
//                    Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
//                    stream(roles).forEach(role -> {
//                        authorities.add(new SimpleGrantedAuthority(role));
//                    });
//                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, null, authorities);
//                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
//                    filterChain.doFilter(httpServletRequest, httpServletResponse);
//                } catch (Exception exception) {
//                    logger.error("\'{}\' tarafından yapılan {} isteği giriş bilgileri hatalı olduğu için reddedildi.", httpServletRequest.getRemoteAddr(), httpServletRequest.getPathInfo());
//                    //TODO : BURASI DEĞİŞTİRİLECEK
//                    httpServletResponse.setHeader("error", exception.getMessage());
//                    httpServletResponse.setStatus(HttpServletResponse.SC_FORBIDDEN);
//                    Map<String, String> error = new HashMap<>();
//                    error.put("errorMessage", exception.getMessage());
//                    httpServletResponse.setContentType(APPLICATION_JSON_VALUE);
//                    new ObjectMapper().writeValue(httpServletResponse.getOutputStream(), error);
//                }
//            } else {
//                logger.error("\'{}\' tarafından yapılan {} isteği giriş yapılmadığı için reddedildi.", httpServletRequest.getRemoteAddr(), httpServletRequest.getServletPath());
//                filterChain.doFilter(httpServletRequest, httpServletResponse);
//            }
//        }
//    }
//}
