package tr.edu.duzce.mf.bm.yancim.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.util.Date;

@Data
public class RoomDTO {
    private Long id;

    private Long maxPlayerCount;
    private String gameTypeName;
    private String cityName;
    private String addressDetails;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date startDate;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
    private Date endDate;

    private String operatorUsername;
}
