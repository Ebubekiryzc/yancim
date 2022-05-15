package tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes;

import lombok.Data;

import java.util.Date;

@Data
public class AccessToken {
    private String token;
    private Date expiration;
}
