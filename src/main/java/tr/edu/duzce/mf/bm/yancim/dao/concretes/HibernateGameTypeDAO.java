package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.GameTypeDAO;
import tr.edu.duzce.mf.bm.yancim.model.GameType;

@Repository
public class HibernateGameTypeDAO extends HibernateMainDAO<GameType> implements GameTypeDAO {
    public HibernateGameTypeDAO() {
        super(GameType.class);
    }

    @Override
    public DataResult<GameType> loadOBjectByName(String name) {
        return super.loadObjectByStringField("name", name);
    }
}
