package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.RoomUser;

import java.util.Locale;

public interface RoomUserService extends MainService<RoomUser> {

    DataResult<RoomUser> loadByUserAndRoomId(Locale locale, Long userId, Long roomId);

    DataResult<Long> loadTotalUserCountForRoom(Locale locale, Long roomId);
}
