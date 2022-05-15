package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoleDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserOperationClaimService;

import java.util.Locale;

@Controller
@RequestMapping(value = "/user_operation_claims")
@CrossOrigin
public class UserOperationClaimController {
    @Autowired
    private UserOperationClaimService userOperationClaimService;

    @GetMapping
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String getAll(Locale locale, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userOperationClaimService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "{id}")
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userOperationClaimService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody RoleDTO roleDTO) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userOperationClaimService.saveOrUpdate(locale, roleDTO));
        return jsonObject.toString();
    }

    @PostMapping(value = "/delete/{id}")
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", userOperationClaimService.delete(locale, id));
        return jsonObject.toString();
    }
}
