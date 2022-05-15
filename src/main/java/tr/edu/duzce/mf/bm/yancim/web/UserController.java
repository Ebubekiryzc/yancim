package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.AccessToken;
import tr.edu.duzce.mf.bm.yancim.model.dto.AuthenticationDTO;
import tr.edu.duzce.mf.bm.yancim.model.dto.UserOperationDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.OperationClaimService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserOperationClaimService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserService;

import java.util.Date;
import java.util.Locale;

// TODO: Burası değişebilir

@Controller
@RequestMapping("/users")
@CrossOrigin
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private UserOperationClaimService userOperationClaimService;
    @Autowired
    private OperationClaimService operationClaimService;

    @PostMapping(value = "login")
    @PermitAll
    public @ResponseBody String login(Locale locale, @RequestBody AuthenticationDTO user) {
        JsonValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);

        DataResult<AccessToken> result = userService.login(locale, user);

        JSONObject resultJson = JSONObject.fromObject(result, jsonConfig);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", resultJson);

        return jsonObject.toString();
    }

    @GetMapping
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String getAll(Locale locale, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "/{id}")
    @PermitAll
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @PermitAll
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody UserOperationDTO user) {
        DataResult<Long> result = userService.saveOrUpdate(locale, user);

        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONObject jsonObject = JSONObject.fromObject(result, jsonConfig);

        return jsonObject.toString();
    }

    @PostMapping(value = "/delete/{id}")
    @PermitAll
    public @ResponseBody String deleteUser(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userService.delete(locale, id));
        return jsonObject.toString();
    }
}
