package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.utilities.result.Result;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoomUserDTO;

import java.util.Locale;

public interface RoomActionService {
    Result addUserToRoom(Locale locale, RoomUserDTO roomUserDTO);

    Result removeUserFromRoom(Locale locale,RoomUserDTO roomUserDTO);
}
