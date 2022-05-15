package tr.edu.duzce.mf.bm.yancim.web;

import net.sf.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.PermitAll;
import tr.edu.duzce.mf.bm.yancim.core.utilities.annotation.RolesAllowed;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.OperationClaimService;

import java.util.Locale;

@Controller
@RequestMapping("/operation_claims")
@CrossOrigin
public class OperationClaimController {
    @Autowired
    private OperationClaimService operationClaimService;

    @GetMapping
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String getAll(Locale locale, @RequestParam(value = "start", required = false) Integer start, @RequestParam(value = "limit", required = false) Integer limit) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", operationClaimService.loadAll(locale, start, limit));
        return jsonObject.toString();
    }

    @GetMapping(value = "{id}")
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String getById(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", operationClaimService.loadById(locale, id));
        return jsonObject.toString();
    }

    @PostMapping(value = "/save_or_update")
    @PermitAll
    public @ResponseBody String saveOrUpdate(Locale locale, @RequestBody OperationClaim operationClaim) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", operationClaimService.saveOrUpdate(locale, operationClaim));
        return jsonObject.toString();
    }

    @PostMapping(value = "/delete/{id}")
    @RolesAllowed(value = {"admin"})
    public @ResponseBody String delete(Locale locale, @PathVariable("id") Long id) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("result", operationClaimService.delete(locale, id));
        return jsonObject.toString();
    }
}
