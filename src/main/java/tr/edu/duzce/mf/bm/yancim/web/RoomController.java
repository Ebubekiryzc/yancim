package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.abstracts.JWTHelper;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.CustomClaim;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoomDTO;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoomUserDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.RoomActionService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.RoomService;

import javax.servlet.http.HttpServletRequest;
import java.util.Locale;

@Controller
@RequestMapping(value = "/rooms")
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomService roomService;

    @Autowired
    private RoomActionService roomActionService;

    @Autowired
    private JWTHelper jwtHelper;

    @GetMapping
    @PermitAll
    public @ResponseBody String getAll(Locale locale, @RequestParam("start") Integer start, @RequestParam("limit") Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "{id}")
    @PermitAll
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @RolesAllowed(value = {"admin", "user"})
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody RoomDTO roomDTO, HttpServletRequest request) {
        // İşlemi yapan kişi tokenden ayrıştırılıyor.
        CustomClaim claim = jwtHelper.verify(request.getHeader(HttpHeaders.AUTHORIZATION));
        roomDTO.setOperatorUsername(claim.getUsername());

        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomService.saveOrUpdate(locale, roomDTO));
        return jsonObject.toString();
    }

    @PostMapping(value = "/join_room")
    @RolesAllowed(value = {"admin", "user"})
    public @ResponseBody String joinRoom(Locale locale, @RequestBody RoomUserDTO roomUserDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomActionService.addUserToRoom(locale, roomUserDTO));
        return jsonObject.toString();
    }


    @PostMapping(value = "/delete/{id}")
    @PermitAll
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomService.delete(locale, id));
        return jsonObject.toString();
    }

}
