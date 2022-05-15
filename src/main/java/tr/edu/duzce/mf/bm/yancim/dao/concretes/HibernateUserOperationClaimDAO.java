package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorDataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessDataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.UserOperationClaimDAO;
import tr.edu.duzce.mf.bm.yancim.model.UserOperationClaim;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

@Repository
public class HibernateUserOperationClaimDAO extends HibernateMainDAO<UserOperationClaim> implements UserOperationClaimDAO {
    public HibernateUserOperationClaimDAO() {
        super(UserOperationClaim.class);
    }

    @Override
    public DataResult<UserOperationClaim> loadObjectByUserIdAndClaimId(Long userId, Long claimId) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<UserOperationClaim> criteriaQuery = criteriaBuilder.createQuery(UserOperationClaim.class);

        Root<UserOperationClaim> userOperationClaimRoot = criteriaQuery.from(UserOperationClaim.class);

        Predicate userPredicate = criteriaBuilder.equal(userOperationClaimRoot.get("user").get("id"), userId);
        Predicate claimPredicate = criteriaBuilder.equal(userOperationClaimRoot.get("operationClaim").get("id"), claimId);

        Predicate predicate = criteriaBuilder.and(userPredicate, claimPredicate);

        criteriaQuery.select(userOperationClaimRoot).where(predicate).distinct(true);

        setLastQuery(criteriaQuery);

        Query<UserOperationClaim> query = currentSession.createQuery(criteriaQuery);
        UserOperationClaim userOperationClaim;
        try {
            userOperationClaim = query.getSingleResult();
        } catch (Exception exception) {
            userOperationClaim = null;
            System.out.println("/48 HibernateUserOperationClaimDAO " + exception.getMessage());
            return new ErrorDataResult<>();
        }

        return new SuccessDataResult<>(userOperationClaim, 1L);
    }
}
