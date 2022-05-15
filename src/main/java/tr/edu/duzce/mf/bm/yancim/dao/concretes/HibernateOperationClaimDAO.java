package tr.edu.duzce.mf.bm.yancim.dao.concretes;

import org.hibernate.Session;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;
import tr.edu.duzce.mf.bm.yancim.core.dao.concretes.HibernateMainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorDataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessDataResult;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.OperationClaimDAO;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.UserOperationClaim;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

@Repository
public class HibernateOperationClaimDAO extends HibernateMainDAO<OperationClaim> implements OperationClaimDAO {

    public HibernateOperationClaimDAO() {
        super(OperationClaim.class);
    }

    @Override
    public DataResult<List<OperationClaim>> loadObjectsByUserId(Long userId) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<OperationClaim> criteriaQuery = criteriaBuilder.createQuery(OperationClaim.class);

        Root<UserOperationClaim> userOperationClaimRoot = criteriaQuery.from(UserOperationClaim.class);
        Root<OperationClaim> operationClaimRoot = criteriaQuery.from(OperationClaim.class);

        Predicate claimPredicate = operationClaimRoot.get("id").in(userOperationClaimRoot.get("operationClaim").get("id"));
        Predicate userPredicate = criteriaBuilder.equal(userOperationClaimRoot.get("user").get("id"), userId);

        Predicate predicate = criteriaBuilder.and(claimPredicate, userPredicate);

        criteriaQuery.select(operationClaimRoot).where(predicate).distinct(true);

        setLastQuery(criteriaQuery);

        Query<OperationClaim> query = currentSession.createQuery(criteriaQuery);
        List<OperationClaim> entityList = query.getResultList();

        if (entityList == null) return new ErrorDataResult<>();

        return new SuccessDataResult<>(entityList, getTotalCount());
    }

    @Override
    public DataResult<OperationClaim> loadObjectByName(String name) {
        return super.loadObjectByStringField("name", name);
    }
}
