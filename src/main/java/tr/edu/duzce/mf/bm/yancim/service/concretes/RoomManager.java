package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.exception.NotAllowedException;
import tr.edu.duzce.mf.bm.yancim.core.utilities.business.BusinessRules;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.RoomDAO;
import tr.edu.duzce.mf.bm.yancim.exception.classes.ResourceNotFoundException;
import tr.edu.duzce.mf.bm.yancim.model.*;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoomDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class RoomManager implements RoomService {

    @Autowired
    private RoomDAO roomDAO;

    @Autowired
    private CityService cityService;

    @Autowired
    private GameTypeService gameTypeService;

    @Autowired
    private RoomUserService roomUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationClaimService operationClaimService;

    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<Room> rooms = roomDAO.loadAllObjects(start, limit);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, new DateJSONValueProcessor("dd/MM/yyyy HH:mm"));
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(rooms, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            Room room = rooms.get(i);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.put("gameType", room.getGameType().getName());
            jsonObject.put("city", room.getCity().getName());
        }
        Long totalCount = roomDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("rooms.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, roomDAO.loadObjectById(id));
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, RoomDTO roomDTO) {
        Room roomToSave = roomDAO.loadObjectById(roomDTO.getId());
        String message;

        DataResult<City> cityResult = getCityFromDTO(locale, roomDTO);
        DataResult<GameType> gameTypeResult = getGameTypeFromDTO(locale, roomDTO);
        DataResult<User> operatorResult = getOperatorFromDTO(locale, roomDTO);

        // CREATE
        if (roomToSave == null) {
            message = messageSource.getMessage("rooms.create", null, locale);

            Result totalResult = BusinessRules.check(cityResult, gameTypeResult, operatorResult);
            if (!totalResult.isSuccess())
                return new ErrorDataResult<>(messageSource.getMessage("rooms.saveHasError", null, locale));

            roomToSave = new Room();
            roomToSave.setId(0L);
            roomToSave.setCity(cityResult.getData());
            roomToSave.setGameType(gameTypeResult.getData());

            setRoomFields(roomDTO, roomToSave);
            DataResult<Long> result = roomDAO.saveOrUpdateObject(roomToSave);

            roomToSave.setId(result.getData());

            RoomUser roomUser = new RoomUser();
            roomUser.setId(0L);
            roomUser.setRoom(roomToSave);
            roomUser.setUser(operatorResult.getData());

            JSONObject jsonClaim = operationClaimService.loadObjectByName(locale, "owner").getData();
            OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(jsonClaim, OperationClaim.class);

            roomUser.setOperationClaim(operationClaim);
            DataResult<Long> roomUserResult = roomUserService.saveOrUpdate(locale, roomUser);
            if (!roomUserResult.isSuccess()) throw new ResourceNotFoundException();

            if (result.isSuccess()) return new SuccessDataResult<>(message);
        }
        // UPDATE
        else {
            message = messageSource.getMessage("rooms.update", null, locale);
            Result isOwner = isOwnerFromDTO(locale, roomDTO);
            Result isAdmin = isOperatorAnAdmin(locale, operatorResult.getData());

            if (isOwner.isSuccess() || isAdmin.isSuccess()) {

                if (cityResult.isSuccess()) {
                    roomToSave.setCity(cityResult.getData());
                }

                if (gameTypeResult.isSuccess()) {
                    roomToSave.setGameType(gameTypeResult.getData());
                }

                if (roomDTO.getMaxPlayerCount() == null) roomDTO.setMaxPlayerCount(roomDTO.getMaxPlayerCount());
                if (roomDTO.getAddressDetails() == null) roomDTO.setAddressDetails(roomToSave.getAddressDetails());
                if (roomDTO.getStartDate() == null) roomDTO.setStartDate(roomToSave.getStartDate());
                if (roomDTO.getEndDate() == null) roomDTO.setEndDate(roomDTO.getEndDate());

                setRoomFields(roomDTO, roomToSave);

                DataResult<Long> result = roomDAO.saveOrUpdateObject(roomToSave);
                if (result.isSuccess()) return new SuccessDataResult<>(message);
            } else {
                throw new NotAllowedException();
            }
        }
        message = messageSource.getMessage("rooms.saveHasError", null, locale);
        return new ErrorDataResult<>(message);
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        Room result = roomDAO.loadObjectById(id);
        if (result != null) {
            return new ErrorResult(messageSource.getMessage("rooms.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("rooms.removeCompleted", null, locale));
    }

    private DataResult<JSONObject> checkIfNull(Locale locale, Room room) {
        if (room == null) return new ErrorDataResult<>(messageSource.getMessage("rooms.notFound", null, locale));
        else {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, new DateJSONValueProcessor("dd/MM/yyyy HH:mm"));
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(room, jsonConfig);
            jsonObject.put("gameType", room.getGameType().getName());
            jsonObject.put("city", room.getCity().getName());

            return new SuccessDataResult<>(messageSource.getMessage("rooms.getSingleResult", null, locale), jsonObject, 1L);
        }
    }

    private DataResult<GameType> getGameTypeFromDTO(Locale locale, RoomDTO roomDTO) {
        GameType gameType = null;
        if (roomDTO.getGameTypeName() != null) {
            DataResult<JSONObject> result = gameTypeService.loadByName(locale, roomDTO.getGameTypeName());
            if (!result.isSuccess()) return new ErrorDataResult<>(result.getMessage(), gameType, 0L);

            JSONObject gameTypeJson = result.getData();
            gameType = (GameType) JSONObject.toBean(gameTypeJson, GameType.class);
            return new SuccessDataResult<>(result.getMessage(), gameType, 1L);
        }

        return new ErrorDataResult<>();
    }

    private DataResult<City> getCityFromDTO(Locale locale, RoomDTO roomDTO) {
        City city = null;
        if (roomDTO.getCityName() != null) {
            DataResult<JSONObject> result = cityService.loadByName(locale, roomDTO.getCityName());
            if (!result.isSuccess()) return new ErrorDataResult<>(result.getMessage(), city, 0L);

            JSONObject cityJson = result.getData();
            city = (City) JSONObject.toBean(cityJson, City.class);
            return new SuccessDataResult<>(result.getMessage(), city, 1L);
        }
        return new ErrorDataResult<>();
    }

    private DataResult<User> getOperatorFromDTO(Locale locale, RoomDTO roomDTO) {
        User user = null;
        if (roomDTO.getOperatorUsername() != null) {
            DataResult<JSONObject> result = userService.loadByUsername(locale, roomDTO.getOperatorUsername());
            if (!result.isSuccess()) return new ErrorDataResult<>(result.getMessage(), user, 0L);

            JSONObject userJson = result.getData();
            user = (User) JSONObject.toBean(userJson, User.class);
            return new SuccessDataResult<>(result.getMessage(), user, 1L);
        }
        return new ErrorDataResult<>();
    }

    private Result isOwnerFromDTO(Locale locale, RoomDTO roomDTO) {
        DataResult<User> userResult = getOperatorFromDTO(locale, roomDTO);
        if (!userResult.isSuccess()) return new ErrorResult();

        RoomUser roomUser = roomUserService.loadByUserAndRoomId(locale, userResult.getData().getId(), roomDTO.getId()).getData();
        if (roomUser.getOperationClaim().getName().equals("owner")) {
            return new SuccessResult();
        } else {
            return new ErrorResult();
        }
    }

    private Result isOperatorAnAdmin(Locale locale, User operator) {
        List<OperationClaim> operationClaims = new ArrayList<>();
        DataResult<JSONArray> result = operationClaimService.loadObjectsByUserId(locale, operator.getId());

        for (int i = 0; i < result.getData().size(); i++) {
            JSONObject claimJson = result.getData().getJSONObject(i);
            OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(claimJson, OperationClaim.class);
            operationClaims.add(operationClaim);
        }

        for (OperationClaim operationClaim : operationClaims) {
            if (operationClaim.getName().equals("admin")) {
                return new SuccessResult();
            }
        }
        return new ErrorResult();
    }

    private void setRoomFields(RoomDTO roomDTO, Room roomToSave) {
        roomToSave.setAddressDetails(roomDTO.getAddressDetails());
        roomToSave.setMaxPlayerCount(roomDTO.getMaxPlayerCount());
        roomToSave.setStartDate(roomDTO.getStartDate());
        roomToSave.setEndDate(roomDTO.getEndDate());
    }

}
