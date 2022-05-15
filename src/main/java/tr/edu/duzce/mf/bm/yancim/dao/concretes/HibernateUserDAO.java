package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.UserDAO;
import tr.edu.duzce.mf.bm.yancim.model.User;

@Repository
public class HibernateUserDAO extends HibernateMainDAO<User> implements UserDAO {

    public HibernateUserDAO() {
        super(User.class);
    }

    @Override
    public DataResult<User> loadObjectByUsername(String username) {
        return loadObjectByStringField("username", username);
    }
}
