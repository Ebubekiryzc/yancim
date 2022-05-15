package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.CityDAO;
import tr.edu.duzce.mf.bm.yancim.model.City;

@Repository
public class HibernateCityDAO extends HibernateMainDAO<City> implements CityDAO {
    public HibernateCityDAO() {
        super(City.class);
    }

    @Override
    public DataResult<City> loadObjectByName(String name) {
        return super.loadObjectByStringField("name", name);
    }
}
