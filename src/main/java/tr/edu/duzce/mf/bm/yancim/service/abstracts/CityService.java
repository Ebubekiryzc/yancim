package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.City;

import java.util.Locale;

public interface CityService extends MainService<City> {
    DataResult<JSONObject> loadByName(Locale locale, String name);
}
