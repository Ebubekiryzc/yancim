package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.business.BusinessRules;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.UserOperationClaimDAO;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.User;
import tr.edu.duzce.mf.bm.yancim.model.UserOperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.dto.RoleDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.OperationClaimService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserOperationClaimService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserService;

import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class UserOperationClaimManager implements UserOperationClaimService {

    @Autowired
    private UserOperationClaimDAO userOperationClaimDAO;

    @Autowired
    private OperationClaimService operationClaimService;

    @Autowired
    private UserService userService;


    @Autowired
    private MessageSource messageSource;


    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<UserOperationClaim> userOperationClaims = userOperationClaimDAO.loadAllObjects(start, limit);
        DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(userOperationClaims, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);

            User user = (User) JSONObject.toBean(JSONObject.fromObject(jsonObject.get("user")), User.class);
            OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(JSONObject.fromObject(jsonObject.get("operationClaim")), OperationClaim.class);

            jsonObject.put("user", user.getUsername());
            jsonObject.put("operationClaim", operationClaim.getName());
        }

        Long totalCount = userOperationClaimDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("userOperationClaims.getAll", null, locale), jsonArray, totalCount);

    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, userOperationClaimDAO.loadObjectById(id));
    }

    @Override
    public DataResult<JSONObject> loadObjectByUserIdAndClaimId(Locale locale, Long userId, Long claimId) {
        return checkIfNull(locale, userOperationClaimDAO.loadObjectByUserIdAndClaimId(userId, claimId).getData());
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, RoleDTO roleDTO) {
        UserOperationClaim userOperationClaimToSave = userOperationClaimDAO.loadObjectById(roleDTO.getId());
        String message;

        DataResult<JSONObject> claimExist = operationClaimService.loadObjectByName(locale, roleDTO.getOperationClaimName());
        DataResult<JSONObject> userExist = userService.loadByUsername(locale, roleDTO.getUsername());


        Result existenceResult = BusinessRules.check(claimExist, userExist);
        if (!existenceResult.isSuccess())
            return new ErrorDataResult<>(messageSource.getMessage("userOperationClaims.saveHasError", null, locale));

        OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(claimExist.getData(), OperationClaim.class);
        User user = (User) JSONObject.toBean(userExist.getData(), User.class);

        if (userOperationClaimToSave == null) {
            message = messageSource.getMessage("userOperationClaims.create", null, locale);
            userOperationClaimToSave = new UserOperationClaim();
            userOperationClaimToSave.setUser(user);
            userOperationClaimToSave.setOperationClaim(operationClaim);
        } else {
            message = messageSource.getMessage("userOperationClaims.update", new Object[]{userOperationClaimToSave.toString(), roleDTO.toString()}, locale);
            userOperationClaimToSave.setUser(user);
            userOperationClaimToSave.setOperationClaim(operationClaim);
        }

        DataResult<Long> result = userOperationClaimDAO.saveOrUpdateObject(userOperationClaimToSave);

        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("userOperationClaims.saveHasError", null, locale));
        }
        return new SuccessDataResult(message, result.getData(), 1L);
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        UserOperationClaim userOperationClaimToRemove = userOperationClaimDAO.loadObjectById(id);
        if (!userOperationClaimDAO.removeObject(userOperationClaimToRemove)) {
            return new ErrorResult(messageSource.getMessage("userOperationClaims.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("userOperationClaims.removeCompleted", new Object[]{userOperationClaimToRemove.getId()}, locale));

    }

    private DataResult<JSONObject> checkIfNull(Locale locale, UserOperationClaim userOperationClaim) {
        if (userOperationClaim == null)
            return new ErrorDataResult<>(messageSource.getMessage("userOperationClaims.notFound", null, locale));
        else {
            DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, processor);
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(userOperationClaim, jsonConfig);

            User user = (User) jsonObject.get("user");
            OperationClaim operationClaim = (OperationClaim) jsonObject.get("operationClaim");

            jsonObject.put("user", user.getUsername());
            jsonObject.put("operationClaim", operationClaim.getName());

            return new SuccessDataResult<>(messageSource.getMessage("userOperationClaims.getSingleResult", new Object[]{userOperationClaim.getId()}, locale), jsonObject, 1L);
        }
    }
}
