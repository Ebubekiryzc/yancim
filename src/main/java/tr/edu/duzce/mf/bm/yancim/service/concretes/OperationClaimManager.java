package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.OperationClaimDAO;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.OperationClaimService;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class OperationClaimManager implements OperationClaimService {

    @Autowired
    private OperationClaimDAO operationClaimDAO;

    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<OperationClaim> operationClaims = operationClaimDAO.loadAllObjects(start, limit);
        DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(operationClaims, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.remove("userOperationClaims");
        }

        Long totalCount = operationClaimDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("operationClaims.getAll", null, locale), jsonArray, totalCount);
    }


    @Override
    public DataResult<JSONArray> loadObjectsByUserId(Locale locale, Long userId) {
        System.out.println("SELAM");
        DataResult<List<OperationClaim>> operationClaims = operationClaimDAO.loadObjectsByUserId(userId);
        DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(operationClaims.getData(), jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.remove("userOperationClaims");
        }

        Long totalCount = operationClaimDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("operationClaims.getAllByUserId", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadObjectByName(Locale locale, String name) {
        DataResult<OperationClaim> claimDataResult = operationClaimDAO.loadObjectByName(name);
        return checkIfNull(locale, claimDataResult.getData());
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, operationClaimDAO.loadObjectById(id));
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, OperationClaim operationClaim) {
        OperationClaim operationClaimToSave = operationClaimDAO.loadObjectById(operationClaim.getId());
        String message;
        if (operationClaimToSave == null) {
            message = messageSource.getMessage("operationClaims.create", new Object[]{operationClaim.getName()}, locale);
            operationClaimToSave = operationClaim;
        } else {
            message = messageSource.getMessage("operationClaims.update", new Object[]{operationClaimToSave.toString(), operationClaim.toString()}, locale);
            operationClaimToSave.setName(operationClaim.getName());
        }
        DataResult<Long> result = operationClaimDAO.saveOrUpdateObject(operationClaimToSave);
        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("operationClaims.saveHasError", null, locale));
        }
        return new SuccessDataResult(message, result.getData(), 1L);
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        OperationClaim operationClaimToRemove = operationClaimDAO.loadObjectById(id);
        if (!operationClaimDAO.removeObject(operationClaimToRemove)) {
            return new ErrorResult(messageSource.getMessage("operationClaims.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("operationClaims.removeCompleted", new Object[]{operationClaimToRemove.getName()}, locale));
    }

    private DataResult<JSONObject> checkIfNull(Locale locale, OperationClaim operationClaim) {
        if (operationClaim == null)
            return new ErrorDataResult<>(messageSource.getMessage("operationClaims.notFound", null, locale));
        else {
            DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, processor);
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(operationClaim, jsonConfig);
            jsonObject.remove("userOperationClaims");
            return new SuccessDataResult<>(messageSource.getMessage("operationClaims.getSingleResult", new Object[]{operationClaim.getName()}, locale), jsonObject, 1L);
        }
    }
}
