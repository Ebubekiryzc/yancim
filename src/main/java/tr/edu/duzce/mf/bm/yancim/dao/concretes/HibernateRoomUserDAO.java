package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorDataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessDataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.RoomUserDAO;
import tr.edu.duzce.mf.bm.yancim.model.RoomUser;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class HibernateRoomUserDAO extends HibernateMainDAO<RoomUser> implements RoomUserDAO {
    public HibernateRoomUserDAO() {
        super(RoomUser.class);
    }

    @Override
    public DataResult<RoomUser> loadObjectByUserAndRoomId(Long userId, Long roomId) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<RoomUser> criteriaQuery = criteriaBuilder.createQuery(RoomUser.class);

        Root<RoomUser> roomUserRoot = criteriaQuery.from(RoomUser.class);

        // select * from room_users where user_id=userId and room_id=roomId

        Predicate userPredicate = criteriaBuilder.equal(roomUserRoot.get("user").get("id"), userId);
        Predicate roomPredicate = criteriaBuilder.equal(roomUserRoot.get("room").get("id"), roomId);

        Predicate predicate = criteriaBuilder.and(userPredicate, roomPredicate);

        criteriaQuery.select(roomUserRoot).where(predicate).distinct(true);

        setLastQuery(criteriaQuery);

        Query<RoomUser> query = currentSession.createQuery(criteriaQuery);
        RoomUser roomUser;
        try {
            roomUser = query.getSingleResult();
        } catch (Exception exception) {
            roomUser = null;
            System.out.println("/48 HibernateUserOperationClaimDAO " + exception.getMessage());
            return new ErrorDataResult<>();
        }

        return new SuccessDataResult<>(roomUser, 1L);
    }

    @Override
    public DataResult<Long> loadObjectByRoomId(Long roomId) {
        Session currentSession = getCurrentSession();
        return null;
    }
}
