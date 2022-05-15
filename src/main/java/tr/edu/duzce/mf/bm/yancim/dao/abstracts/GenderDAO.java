package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.Gender;

public interface GenderDAO extends MainDAO<Gender> {
    DataResult<Gender> loadByName(String name);
}
