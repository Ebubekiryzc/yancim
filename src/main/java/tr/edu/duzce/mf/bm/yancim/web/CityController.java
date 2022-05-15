package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.model.City;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.CityService;

import java.util.Locale;

@Controller
@RequestMapping(value = "/cities")
@CrossOrigin
public class CityController {
    @Autowired
    private CityService cityService;

    @GetMapping
    @PermitAll
    public @ResponseBody String getAll(Locale locale, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", cityService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "{id}")
    @PermitAll
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", cityService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @RolesAllowed({"admin"})
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody City city) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", cityService.saveOrUpdate(locale, city));
        return jsonObject.toString();
    }

    @PostMapping(value = "/delete/{id}")
    @RolesAllowed({"admin"})
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", cityService.delete(locale, id));
        return jsonObject.toString();
    }
}
