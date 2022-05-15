package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.Result;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessDataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.RoomDAO;
import tr.edu.duzce.mf.bm.yancim.model.Room;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.RoomService;

import java.util.List;
import java.util.Locale;

@Service
public class RoomManager implements RoomService {

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<Room> rooms = roomDAO.loadAllObjects(start, limit);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(rooms, jsonConfig);

        Long totalCount = roomDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("rooms.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return null;
    }

    @Override
    public DataResult<Long> saveOrUpdate(Locale locale, Room room) {
        return null;
    }

    @Override
    public Result delete(Locale locale, Long id) {
        return null;
    }
}
