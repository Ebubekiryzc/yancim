package tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes;

import lombok.Data;

import java.util.ArrayList;

@Data
public class CustomClaim {
    private String username;
    private ArrayList<String> roles;
}
