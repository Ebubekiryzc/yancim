package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.model.GameType;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.GameTypeService;

import java.util.Locale;

@Controller
@RequestMapping(value = "/game_types")
@CrossOrigin
public class GameTypeController {
    @Autowired
    private GameTypeService gameTypeService;

    @GetMapping
    @PermitAll
    public @ResponseBody String getAll(Locale locale, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", gameTypeService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "{id}")
    @PermitAll
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", gameTypeService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @RolesAllowed({"admin"})
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody GameType gameType) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", gameTypeService.saveOrUpdate(locale, gameType));
        return jsonObject.toString();
    }

    @PostMapping(value = "/delete/{id}")
    @RolesAllowed({"admin"})
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", gameTypeService.delete(locale, id));
        return jsonObject.toString();
    }
}
