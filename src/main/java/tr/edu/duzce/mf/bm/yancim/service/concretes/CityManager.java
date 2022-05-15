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
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.CityDAO;
import tr.edu.duzce.mf.bm.yancim.model.City;
import tr.edu.duzce.mf.bm.yancim.model.Room;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.CityService;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Service
public class CityManager implements CityService {

    @Autowired
    private CityDAO cityDAO;

    @Autowired
    private MessageSource messageSource;


    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<City> cities = cityDAO.loadAllObjects(start, limit);
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(cities, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            City city = cities.get(i);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            List<Long> roomIds = new ArrayList<>();

            for (Room room : city.getRooms()) {
                roomIds.add(room.getId());
            }

            JSONArray roomArray = JSONArray.fromObject(roomIds);
            jsonObject.put("rooms", roomArray);
        }
        Long totalCount = cityDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("cities.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, cityDAO.loadObjectById(id));
    }

    @Override
    public DataResult<JSONObject> loadByName(Locale locale, String name) {
        return checkIfNull(locale, cityDAO.loadObjectByName(name).getData());
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, City city) {
        City cityToSave = cityDAO.loadObjectById(city.getId());
        String message;
        if (cityToSave == null) {
            message = messageSource.getMessage("cities.create", new Object[]{city.getName()}, locale);
            cityToSave = city;
        } else {
            message = messageSource.getMessage("cities.update", new Object[]{cityToSave.getName()}, locale);
            cityToSave.setName(city.getName());
        }
        DataResult<Long> result = cityDAO.saveOrUpdateObject(cityToSave);
        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("cities.saveHasError", null, locale));
        }
        return new SuccessDataResult<>(message, result.getData(), result.getTotalDataCount());
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        City cityToRemove = cityDAO.loadObjectById(id);
        if (!cityDAO.removeObject(cityToRemove)) {
            return new ErrorResult(messageSource.getMessage("cities.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("cities.removeCompleted", new Object[]{cityToRemove.getName()}, locale));

    }

    private DataResult<JSONObject> checkIfNull(Locale locale, City city) {
        if (city == null)
            return new ErrorDataResult<>(messageSource.getMessage("cities.notFound", null, locale));
        else {
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(city, jsonConfig);
            List<Long> roomIds = new ArrayList<>();
            for (Room room : city.getRooms()) {
                roomIds.add(room.getId());
            }
            JSONArray roomArray = JSONArray.fromObject(roomIds);
            jsonObject.put("rooms", roomArray);
            return new SuccessDataResult<>(messageSource.getMessage("cities.getSingleResult", new Object[]{city.getName()}, locale), jsonObject, 1L);
        }
    }
}
