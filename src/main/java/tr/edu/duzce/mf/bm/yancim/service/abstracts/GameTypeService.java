package tr.edu.duzce.mf.bm.yancim.service.abstracts;

import net.sf.json.JSONObject;
import tr.edu.duzce.mf.bm.yancim.core.service.abstracts.MainService;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.model.GameType;

import java.util.Locale;

public interface GameTypeService extends MainService<GameType> {
    DataResult<JSONObject> loadByName(Locale locale, String name);
}
