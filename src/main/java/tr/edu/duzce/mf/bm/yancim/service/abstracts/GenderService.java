package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.Gender;

import java.util.Locale;

public interface GenderService extends MainService<Gender> {
    DataResult<JSONObject> loadByName(Locale locale, String name);
}
