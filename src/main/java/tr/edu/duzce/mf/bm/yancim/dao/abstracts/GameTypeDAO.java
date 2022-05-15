package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.GameType;

public interface GameTypeDAO extends MainDAO<GameType> {
    DataResult<GameType> loadOBjectByName(String name);
}
