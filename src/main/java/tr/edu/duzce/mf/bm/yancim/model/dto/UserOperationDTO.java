package tr.edu.duzce.mf.bm.yancim.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOperationDTO implements Serializable {
    private Long id;
    private String genderName;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private String password;

    private String operatorUserName;
}
