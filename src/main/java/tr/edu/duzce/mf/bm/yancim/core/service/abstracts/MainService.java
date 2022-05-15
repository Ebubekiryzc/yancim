package tr.edu.duzce.mf.bm.yancim.core.service.abstracts;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.Result;

import java.util.Locale;

@Transactional(propagation = Propagation.REQUIRED, readOnly = true, rollbackFor = {Exception.class})
public interface MainService<TEntity> {
    DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit);

    DataResult<JSONObject> loadById(Locale locale, Long id);

    DataResult<Long> saveOrUpdate(Locale locale, TEntity entity);

    Result delete(Locale locale, Long id);
}
