package tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.AccessToken;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.CustomClaim;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.User;

import java.util.List;

public interface JWTHelper {
    AccessToken createToken(User user, List<OperationClaim> operationClaims);

    CustomClaim verify(String authorization);
}
