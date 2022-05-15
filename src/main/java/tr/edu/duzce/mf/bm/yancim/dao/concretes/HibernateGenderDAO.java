package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.GenderDAO;
import tr.edu.duzce.mf.bm.yancim.model.Gender;

@Repository
public class HibernateGenderDAO extends HibernateMainDAO<Gender> implements GenderDAO {

    public HibernateGenderDAO() {
        super(Gender.class);
    }

    @Override
    public DataResult<Gender> loadByName(String name) {
        return loadObjectByStringField("name", name);
    }
}
