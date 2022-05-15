package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.RoomUserDAO;
import tr.edu.duzce.mf.bm.yancim.model.RoomUser;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.RoomUserService;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class RoomUserManager implements RoomUserService {

    @Autowired
    private RoomUserDAO roomUserDAO;

    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<RoomUser> roomUsers = roomUserDAO.loadAllObjects(start, limit);
        DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(roomUsers, jsonConfig);
        Long totalCount = roomUserDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("roomUsers.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, roomUserDAO.loadObjectById(id));
    }

    @Override
    public DataResult<RoomUser> loadByUserAndRoomId(Locale locale, Long userId, Long roomId) {
        return roomUserDAO.loadObjectByUserAndRoomId(userId, roomId);
    }

    @Override
    public DataResult<Long> loadTotalUserCountForRoom(Locale locale, Long roomId) {
        return roomUserDAO.loadObjectByRoomId(roomId);
    }

    @Override
    public DataResult<Long> saveOrUpdate(Locale locale, RoomUser roomUser) {
        RoomUser roomUserToSave = roomUserDAO.loadObjectById(roomUser.getId());
        String message;
        if (roomUserToSave == null) {
            message = messageSource.getMessage("roomUsers.create", null, locale);
            roomUserToSave = new RoomUser();
            roomUserToSave.setId(0L);
            setRoomUserFields(roomUser, roomUserToSave);
        } else {
            message = messageSource.getMessage("roomUsers.update", new Object[]{roomUserToSave.toString(), roomUser.toString()}, locale);
            setRoomUserFields(roomUser, roomUserToSave);
        }
        DataResult<Long> result = roomUserDAO.saveOrUpdateObject(roomUserToSave);
        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("roomUsers.saveHasError", null, locale));
        }
        return new SuccessDataResult(message, result.getData(), 1L);
    }


    @Override
    public Result delete(Locale locale, Long id) {
        RoomUser roomUserToRemove = roomUserDAO.loadObjectById(id);
        if (!roomUserDAO.removeObject(roomUserToRemove)) {
            return new ErrorResult(messageSource.getMessage("roomUsers.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("roomUsers.removeCompleted", null, locale));

    }

    private DataResult<JSONObject> checkIfNull(Locale locale, RoomUser roomUser) {
        if (roomUser == null)
            return new ErrorDataResult<>(messageSource.getMessage("roomUsers.notFound", null, locale));
        else {
            DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, processor);
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(roomUser, jsonConfig);
            return new SuccessDataResult<>(messageSource.getMessage("roomUsers.getSingleResult", null, locale), jsonObject, 1L);
        }
    }

    private void setRoomUserFields(RoomUser roomUser, RoomUser roomUserToSave) {
        roomUserToSave.setRoom(roomUser.getRoom());
        roomUserToSave.setUser(roomUser.getUser());
        roomUserToSave.setOperationClaim(roomUser.getOperationClaim());
    }

}
