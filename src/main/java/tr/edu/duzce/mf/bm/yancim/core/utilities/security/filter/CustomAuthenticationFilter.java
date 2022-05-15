//package tr.edu.duzce.mf.bm.yancim.core.utilities.security.filter;
//
//import com.auth0.jwt.JWT;
//import com.auth0.jwt.algorithms.Algorithm;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.security.authentication.AuthenticationManager;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.AuthenticationException;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.security.core.userdetails.User;
//import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
//
//import javax.servlet.FilterChain;
//import javax.servlet.ServletException;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.io.IOException;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
//
//public class CustomAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
//
//    private final AuthenticationManager authenticationManager;
//    private static Logger logger = LoggerFactory.getLogger(AuthenticationManager.class);
//
//    public CustomAuthenticationFilter(AuthenticationManager authenticationManager) {
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Override
//    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
//        String username = request.getParameter("username");
//        String password = request.getParameter("password");
//        UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(username, password);
//        return authenticationManager.authenticate(authenticationToken);
//    }
//
//    @Override
//    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain, Authentication authentication) throws IOException, ServletException {
//        User user = (User) authentication.getPrincipal();
//        //Normalde saklı bir yerde bu şifreyi tutabiliriz.
//        Algorithm algorithm = Algorithm.HMAC256("secret".getBytes());
//        //100 dakika;
//        String accessToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date((System.currentTimeMillis() + 100 * 60 * 1000))).withIssuer(request.getRequestURL().toString()).withClaim("roles", user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList())).sign(algorithm);
//        //30 dakika;
//        String refreshToken = JWT.create().withSubject(user.getUsername()).withExpiresAt(new Date((System.currentTimeMillis() + 30 * 60 * 1000))).withIssuer(request.getRequestURL().toString()).sign(algorithm);
//
////        response.setHeader("accessToken", accessToken);
////        response.setHeader("refreshToken", refreshToken);
//
//        Map<String, String> tokens = new HashMap<>();
//        tokens.put("accessToken", accessToken);
//        tokens.put("refreshToken", refreshToken);
//        response.setContentType(APPLICATION_JSON_VALUE);
//        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
//
//        logger.info("Login successful!");
//    }
//}
