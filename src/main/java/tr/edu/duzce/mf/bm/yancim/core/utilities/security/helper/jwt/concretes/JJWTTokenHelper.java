package tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.concretes;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import tr.edu.duzce.mf.bm.yancim.core.exception.NotAllowedException;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.abstracts.JWTHelper;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.AccessToken;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.CustomClaim;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


public class JJWTTokenHelper implements JWTHelper {

    private Date expirationDate;
    private final String secretKey = "secret";

    public JJWTTokenHelper() {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date(System.currentTimeMillis()));
        calendar.add(Calendar.HOUR, 1);
        this.expirationDate = calendar.getTime();
    }

    @Override
    public AccessToken createToken(User user, List<OperationClaim> operationClaims) {
        String issuer = "www.yancim.com";
        String audience = issuer;

        Date issuedAt = new Date(System.currentTimeMillis());
        Date expiryAt = expirationDate;

        Claims claims = Jwts.claims()
                .setIssuer(issuer)
                .setAudience(audience)
                .setIssuedAt(issuedAt)
                .setExpiration(expiryAt);

        String[] roles = operationClaims.stream()
                .map(operationClaim -> operationClaim.getName())
                .collect(Collectors.toList())
                .toArray(new String[operationClaims.size()]);

        claims.put("roles", roles);
        claims.put("username", user.getUsername());

        // generate jwt using claims

        String token = Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();

        AccessToken accessToken = new AccessToken();
        accessToken.setToken(token);
        accessToken.setExpiration(expiryAt);

        return accessToken;
    }

    public CustomClaim verify(String authorization) {
        authorization = authorization.split("Bearer ")[1];
        try {
            Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(authorization).getBody();

            CustomClaim customClaim = new CustomClaim();
            customClaim.setUsername(claims.get("username").toString());
            customClaim.setRoles(claims.get("roles", ArrayList.class));

            return customClaim;
        } catch (Exception e) {
            throw new NotAllowedException();
        }
    }

}
