package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.GenderDAO;
import tr.edu.duzce.mf.bm.yancim.model.Gender;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.GenderService;

import java.util.List;
import java.util.Locale;

@Service
public class GenderManager implements GenderService {
    @Autowired
    private GenderDAO genderDAO;
    @Autowired
    private MessageSource messageSource;

    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<Gender> genders = genderDAO.loadAllObjects(start, limit);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(genders, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.remove("users");
        }

        Long totalCount = genderDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("genders.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, genderDAO.loadObjectById(id));
    }

    @Override
    public DataResult<JSONObject> loadByName(Locale locale, String name) {
        return checkIfNull(locale, genderDAO.loadByName(name).getData());
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, Gender gender) {
        Gender genderToSave = genderDAO.loadObjectById(gender.getId());
        String message;
        if (genderToSave == null) {
            message = messageSource.getMessage("genders.create", new Object[]{gender.getName()}, locale);
            genderToSave = gender;
        } else {
            message = messageSource.getMessage("genders.update", new Object[]{genderToSave.toString(), gender.toString()}, locale);
            genderToSave.setName(gender.getName());
        }
        DataResult<Long> result = genderDAO.saveOrUpdateObject(genderToSave);
        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("genders.saveHasError", null, locale));
        }
        return new SuccessDataResult(message, result.getData(), 1L);
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        Gender genderToRemove = genderDAO.loadObjectById(id);
        if (!genderDAO.removeObject(genderToRemove)) {
            return new ErrorResult(messageSource.getMessage("genders.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("genders.removeCompleted", new Object[]{genderToRemove.getName()}, locale));
    }

    private DataResult<JSONObject> checkIfNull(Locale locale, Gender gender) {
        if (gender == null) return new ErrorDataResult<>(messageSource.getMessage("genders.notFound", null, locale));
        else {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(gender, jsonConfig);
            jsonObject.remove("users");
            return new SuccessDataResult<>(messageSource.getMessage("genders.getSingleResult", new Object[]{gender.getName()}, locale), jsonObject, 1L);
        }
    }
}
