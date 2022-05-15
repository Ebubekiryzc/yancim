package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;

import java.util.Locale;

public interface OperationClaimService extends MainService<OperationClaim> {
    DataResult<JSONArray> loadObjectsByUserId(Locale locale, Long userId);

    DataResult<JSONObject> loadObjectByName(Locale locale, String name);
}
