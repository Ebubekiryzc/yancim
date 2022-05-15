package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoleDTO;

import java.util.Locale;

public interface UserOperationClaimService extends MainService<RoleDTO> {
    DataResult<JSONObject> loadObjectByUserIdAndClaimId(Locale locale, Long userId, Long claimId);
}
