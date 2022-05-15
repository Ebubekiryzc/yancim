package tr.edu.duzce.mf.bm.yancim.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.City;

public interface CityDAO extends MainDAO<City> {
    DataResult<City> loadObjectByName(String name);
}
