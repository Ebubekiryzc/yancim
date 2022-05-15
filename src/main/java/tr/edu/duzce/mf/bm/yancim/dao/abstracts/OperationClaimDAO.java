package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;

import java.util.List;

public interface OperationClaimDAO extends MainDAO<OperationClaim> {
    DataResult<List<OperationClaim>> loadObjectsByUserId(Long userId);

    DataResult<OperationClaim> loadObjectByName(String name);
}
