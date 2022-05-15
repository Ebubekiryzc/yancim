package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.AccessToken;
import tr.edu.duzce.mf.bm.yancim.model.dto.AuthenticationDTO;
import tr.edu.duzce.mf.bm.yancim.model.dto.UserOperationDTO;

import java.util.Locale;

public interface UserService extends MainService<UserOperationDTO> {
    DataResult<AccessToken> login(Locale locale, AuthenticationDTO credentials);

    DataResult<JSONObject> loadByUsername(Locale locale, String username);
}
