package tr.edu.duzce.mf.bm.yancim.core.dao.concretes;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.springframework.beans.factory.annotation.Autowired;
import tr.edu.duzce.mf.bm.yancim.core.dao.abstracts.MainDAO;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.DataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.ErrorDataResult;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessDataResult;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.io.Serializable;
import java.util.List;
import java.util.Locale;


public class HibernateMainDAO<TEntity> implements MainDAO<TEntity> {
    @Autowired
    private SessionFactory sessionFactory;

    private Class<TEntity> entityClass;
    private CriteriaQuery<TEntity> lastQuery;

    public HibernateMainDAO(Class<TEntity> entityClass) {
        this.entityClass = entityClass;
    }

    protected Session getCurrentSession() {
        return sessionFactory.getCurrentSession();
    }

    public CriteriaQuery<TEntity> getLastQuery() {
        return lastQuery;
    }

    public void setLastQuery(CriteriaQuery<TEntity> lastQuery) {
        this.lastQuery = lastQuery;
    }

    @Override
    public TEntity loadObjectById(Serializable id) {
        return getCurrentSession().get(entityClass, id);
    }

    @Override
    public DataResult<Long> saveOrUpdateObject(TEntity entity) {
        Long result = null;
        try {
            Serializable id = getCurrentSession().save(entity);
            result = (Long) id;
        } catch (Exception e) {
            e.printStackTrace();
            new ErrorDataResult<Long>(null, 0L);
        }
        return new SuccessDataResult<Long>(result, 1L);
    }

    @Override
    public boolean removeObject(TEntity entity) {
        boolean success = true;
        try {
            getCurrentSession().remove(entity);
        } catch (Exception e) {
            e.printStackTrace();
            success = false;
        }
        return success;
    }

    @Override
    public List<TEntity> loadAllObjects(Integer start, Integer limit) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<TEntity> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<TEntity> root = criteriaQuery.from(entityClass);
        criteriaQuery.select(root);

        Query<TEntity> query = currentSession.createQuery(criteriaQuery);
        setLastQuery(criteriaQuery);

        if (start != null && limit != null) {
            query.setFirstResult(start);
            query.setMaxResults(limit);
        }

        List<TEntity> entityList = query.getResultList();
        return entityList;
    }

    @Override
    public DataResult<TEntity> loadObjectByStringField(String parameter, String value) {
        Session currentSession = getCurrentSession();
        CriteriaBuilder criteriaBuilder = currentSession.getCriteriaBuilder();
        CriteriaQuery<TEntity> criteriaQuery = criteriaBuilder.createQuery(entityClass);
        Root<TEntity> root = criteriaQuery.from(entityClass);
        Predicate predicate = criteriaBuilder.equal(criteriaBuilder.lower(root.<String>get(parameter)), value.toLowerCase(new Locale("tr", "TR")));
        criteriaQuery.select(root).where(predicate);

        Query<TEntity> query = currentSession.createQuery(criteriaQuery);
        TEntity entity;
        try {
            entity = query.getSingleResult();
        } catch (Exception exception) {
            entity = null;
            System.out.println("/109 HibernateMainDAO " + exception.getMessage());
            return new ErrorDataResult<>();
        }

        return new SuccessDataResult<>(entity, 1L);
    }

    @Override
    public Long getTotalCount() {
        Query<TEntity> query = getCurrentSession().createQuery(getLastQuery());
        return query.stream().count();
    }
}
