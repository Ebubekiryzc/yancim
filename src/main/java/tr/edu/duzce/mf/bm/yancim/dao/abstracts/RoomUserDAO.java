package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.RoomUser;

public interface RoomUserDAO extends MainDAO<RoomUser> {
    DataResult<RoomUser> loadObjectByUserAndRoomId(Long userId, Long roomId);

    DataResult<Long> loadObjectByRoomId(Long roomId);
}
