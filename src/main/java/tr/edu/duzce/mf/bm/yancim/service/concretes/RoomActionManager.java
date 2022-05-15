package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.Result;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessResult;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.Room;
import tr.edu.duzce.mf.bm.yancim.model.RoomUser;
import tr.edu.duzce.mf.bm.yancim.model.User;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoomUserDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.*;

import java.util.Locale;

@Service
public class RoomActionManager implements RoomActionService {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomUserService roomUserService;

    @Autowired
    private UserService userService;

    @Autowired
    private OperationClaimService operationClaimService;

    @Autowired
    private MessageSource messageSource;


    @Override
    public Result addUserToRoom(Locale locale, RoomUserDTO roomUserDTO) {
        JSONObject userJson = userService.loadByUsername(locale, roomUserDTO.getUsername()).getData();
        User user = (User) JSONObject.toBean(userJson, User.class);

        JSONObject roomJson = roomService.loadById(locale, roomUserDTO.getRoomId()).getData();
        Room room = (Room) JSONObject.toBean(roomJson, Room.class);

        JSONObject claimJson = operationClaimService.loadObjectByName(locale, "user").getData();
        OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(claimJson, OperationClaim.class);


        RoomUser roomUser = new RoomUser();
        roomUser.setId(0L);
        roomUser.setUser(user);
        roomUser.setRoom(room);
        roomUser.setOperationClaim(operationClaim);
        Result result = roomUserService.saveOrUpdate(locale, roomUser);
        if (!result.isSuccess())
            return new ErrorResult(messageSource.getMessage("roomUsers.saveCompleted", null, locale));
        return new SuccessResult(messageSource.getMessage("roomUsers.saveHasError", null, locale));
    }

    @Override
    public Result removeUserFromRoom(Locale locale, RoomUserDTO roomUserDTO) {
        return null;
    }
}
