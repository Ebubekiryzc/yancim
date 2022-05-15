package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.GameTypeDAO;
import tr.edu.duzce.mf.bm.yancim.model.GameType;
import tr.edu.duzce.mf.bm.yancim.model.Room;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.GameTypeService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class GameTypeManager implements GameTypeService {

    @Autowired
    private GameTypeDAO gameTypeDAO;
    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<GameType> gameTypes = gameTypeDAO.loadAllObjects(start, limit);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(gameTypes, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            GameType gameType = gameTypes.get(i);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<Long> roomIds = new ArrayList<>();

            for (Room room : gameType.getRooms()) {
                roomIds.add(room.getId());
            }

            JSONArray roomArray = JSONArray.fromObject(roomIds);
            jsonObject.put("rooms", roomArray);
        }
        Long totalCount = gameTypeDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("gameTypes.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, gameTypeDAO.loadObjectById(id));
    }

    @Override
    public DataResult<JSONObject> loadByName(Locale locale, String name) {
        return checkIfNull(locale, gameTypeDAO.loadOBjectByName(name).getData());
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, GameType gameType) {
        GameType gameTypeToSave = gameTypeDAO.loadObjectById(gameType.getId());
        String message;
        if (gameTypeToSave == null) {
            message = messageSource.getMessage("gameTypes.create", new Object[]{gameType.getName()}, locale);
            gameTypeToSave = gameType;
        } else {
            message = messageSource.getMessage("gameTypes.update", new Object[]{gameTypeToSave.getName()}, locale);
            gameTypeToSave.setName(gameType.getName());
        }

        DataResult<Long> result = gameTypeDAO.saveOrUpdateObject(gameTypeToSave);
        if (!result.isSuccess()) {
            return new ErrorDataResult<>(messageSource.getMessage("gameTypes.saveHasError", null, locale));
        }
        return new SuccessDataResult<>(message, result.getData(), result.getTotalDataCount());
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        GameType gameTypeToRemove = gameTypeDAO.loadObjectById(id);
        if (!gameTypeDAO.removeObject(gameTypeToRemove)) {
            return new ErrorResult(messageSource.getMessage("gameTypes.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("gameTypes.removeCompleted", new Object[]{gameTypeToRemove.getName()}, locale));

    }

    private DataResult<JSONObject> checkIfNull(Locale locale, GameType gameType) {
        if (gameType == null)
            return new ErrorDataResult<>(messageSource.getMessage("gameTypes.notFound", null, locale));
        else {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(gameType, jsonConfig);
            List<Long> roomIds = new ArrayList<>();
            for (Room room : gameType.getRooms()) {
                roomIds.add(room.getId());
            }
            jsonObject.put("rooms", roomIds);
            return new SuccessDataResult<>(messageSource.getMessage("gameTypes.getSingleResult", new Object[]{gameType.getName()}, locale), jsonObject, 1L);
        }
    }
}
