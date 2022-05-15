package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.UserOperationClaim;

public interface UserOperationClaimDAO extends MainDAO<UserOperationClaim> {
    DataResult<UserOperationClaim> loadObjectByUserIdAndClaimId(Long userId, Long claimId);
}
