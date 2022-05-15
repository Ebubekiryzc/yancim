package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.RoomService;

import java.util.Locale;

@Controller
@RequestMapping(value = "/rooms")
@CrossOrigin
public class RoomController {

    @Autowired
    private RoomService roomService;

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


    @PostMapping(value = "/delete/{id}")
    @PermitAll
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", roomService.delete(locale, id));
        return jsonObject.toString();
    }

}
