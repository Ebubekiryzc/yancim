package tr.edu.duzce.mf.bm.yancim.core.dao.abstracts;

import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;

import java.io.Serializable;
import java.util.List;

public interface MainDAO<TEntity> {
    TEntity loadObjectById(Serializable id);

    DataResult<Long> saveOrUpdateObject(TEntity entity);

    boolean removeObject(TEntity entity);

    List<TEntity> loadAllObjects(Integer start, Integer limit);

    DataResult<TEntity> loadObjectByStringField(String parameter, String value);

    Long getTotalCount();
}
