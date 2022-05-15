package tr.edu.duzce.mf.bm.yancim.model.dto;

import lombok.Data;

@Data
public class RoomUserDTO {
    private String username;
    private Long roomId;

    private String operatorUsername;
}
