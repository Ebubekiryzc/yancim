package tr.edu.duzce.mf.bm.yancim.service.concretes;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter.DateJSONValueProcessor;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.*;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.abstracts.JWTHelper;
import tr.edu.duzce.mf.bm.yancim.core.utilities.security.helper.jwt.classes.AccessToken;
import tr.edu.duzce.mf.bm.yancim.dao.abstracts.UserDAO;
import tr.edu.duzce.mf.bm.yancim.model.Gender;
import tr.edu.duzce.mf.bm.yancim.model.OperationClaim;
import tr.edu.duzce.mf.bm.yancim.model.User;
import tr.edu.duzce.mf.bm.yancim.model.dto.AuthenticationDTO;
import tr.edu.duzce.mf.bm.yancim.model.dto.UserOperationDTO;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.GenderService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.OperationClaimService;
import tr.edu.duzce.mf.bm.yancim.service.abstracts.UserService;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Service
public class UserManager implements UserService {

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private GenderService genderService;

    @Autowired
    private OperationClaimService operationClaimService;

    @Autowired
    private MessageSource messageSource;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JWTHelper jwtHelper;

    @Override
    public DataResult<AccessToken> login(Locale locale, AuthenticationDTO credentials) {
        DataResult<User> userDataResult = userDAO.loadObjectByUsername(credentials.getUsername());

        // Kullanıcı var mı?
        if (!userDataResult.isSuccess())
            return new ErrorDataResult<>(messageSource.getMessage("users.notFound", null, locale));

        User user = userDataResult.getData();


        // Kullanıcının şifresi aynı mı?
        if (!passwordEncoder.matches(credentials.getPassword(), user.getPassword()))
            return new ErrorDataResult<>(messageSource.getMessage("users.passwordNotMatch", null, locale));

        List<OperationClaim> userClaims = new ArrayList<>();

        // Kullanıcının yetkilerini elde ediyoruz.
        DataResult<JSONArray> claimResult = operationClaimService.loadObjectsByUserId(locale, user.getId());
        for (int index = 0; index < claimResult.getTotalDataCount(); index++) {
            JSONObject jsonClaim = claimResult.getData().getJSONObject(index);
            OperationClaim operationClaim = (OperationClaim) JSONObject.toBean(jsonClaim, OperationClaim.class);
            userClaims.add(operationClaim);
        }

        AccessToken accessToken = jwtHelper.createToken(user, userClaims);
        return new SuccessDataResult<>(messageSource.getMessage("users.loginSuccessful", null, locale), accessToken, 1L);
    }


    @Override
    public DataResult<JSONArray> loadAll(Locale locale, Integer start, Integer limit) {
        List<User> users = userDAO.loadAllObjects(start, limit);
        DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.registerJsonValueProcessor(Date.class, processor);
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONArray jsonArray = JSONArray.fromObject(users, jsonConfig);

        for (int i = 0; i < jsonArray.size(); i++) {
            User user = users.get(i);
            JSONObject jsonObject = jsonArray.getJSONObject(i);
            jsonObject.put("gender", user.getGender().getName());
            jsonObject.remove("password");
            jsonObject.remove("userOperationClaims");
        }

        Long totalCount = userDAO.getTotalCount();
        return new SuccessDataResult<>(messageSource.getMessage("users.getAll", null, locale), jsonArray, totalCount);
    }

    @Override
    public DataResult<JSONObject> loadById(Locale locale, Long id) {
        return checkIfNull(locale, userDAO.loadObjectById(id));
    }

    @Override
    public DataResult<JSONObject> loadByUsername(Locale locale, String username) {
        return checkIfNull(locale, userDAO.loadObjectByUsername(username).getData());
    }

    @Override
    @Transactional
    public DataResult<Long> saveOrUpdate(Locale locale, UserOperationDTO user) {
        User userToSave = userDAO.loadObjectById(user.getId());
        String message;
        if (userToSave == null) {
            message = messageSource.getMessage("users.create", new Object[]{user.getUsername()}, locale);

            DataResult<JSONObject> genderResult = genderService.loadByName(locale, user.getGenderName());
            if (!genderResult.isSuccess()) return new ErrorDataResult<>(genderResult.getMessage());

            userToSave = new User();

            setUserFields(user, userToSave, genderResult);
            userToSave.setPassword(passwordEncoder.encode(user.getPassword()));

        } else {
            message = messageSource.getMessage("users.update", new Object[]{userToSave.getUsername()}, locale);

            DataResult<JSONObject> genderResult = genderService.loadByName(locale, user.getGenderName());
            if (!genderResult.isSuccess()) return new ErrorDataResult<>(genderResult.getMessage());

            if (user.getGenderName() == null) user.setGenderName(userToSave.getGender().getName());
            if (user.getUsername() == null) user.setUsername(userToSave.getUsername());
            if (user.getEmail() == null) user.setUsername(userToSave.getEmail());
            if (user.getFirstName() == null) user.setFirstName(userToSave.getFirstName());
            if (user.getLastName() == null) user.setLastName(userToSave.getLastName());

            if (user.getPassword() != null)
                userToSave.setPassword(passwordEncoder.encode(user.getPassword()));

            setUserFields(user, userToSave, genderResult);
        }
        DataResult<Long> result = userDAO.saveOrUpdateObject(userToSave);

        if (!result.isSuccess()) {
            return new ErrorDataResult(messageSource.getMessage("users.saveHasError", null, locale));
        }
        return new SuccessDataResult(message, result.getData(), 1L);
    }

    @Override
    @Transactional
    public Result delete(Locale locale, Long id) {
        User userToRemove = userDAO.loadObjectById(id);
        if (!userDAO.removeObject(userToRemove)) {
            return new ErrorResult(messageSource.getMessage("users.removeHasError", null, locale));
        }
        return new SuccessResult(messageSource.getMessage("users.removeCompleted", new Object[]{userToRemove.getUsername()}, locale));
    }

    private void setUserFields(UserOperationDTO user, User userToSave, DataResult<JSONObject> genderResult) {
        Gender gender = (Gender) JSONObject.toBean(genderResult.getData(), Gender.class);
        userToSave.setFirstName(user.getFirstName());
        userToSave.setLastName(user.getLastName());
        userToSave.setEmail(user.getEmail());
        userToSave.setUsername(user.getUsername());
        userToSave.setGender(gender);
    }

    private DataResult<JSONObject> checkIfNull(Locale locale, User user) {
        if (user == null) return new ErrorDataResult<>(messageSource.getMessage("users.notFound", null, locale));
        else {
            DateJSONValueProcessor processor = new DateJSONValueProcessor("dd/MM/yyyy HH:mm");
            JsonConfig jsonConfig = new JsonConfig();
            jsonConfig.registerJsonValueProcessor(Date.class, processor);
            jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
            JSONObject jsonObject = JSONObject.fromObject(user, jsonConfig);
            jsonObject.remove("password");
//            jsonObject.remove("userOperationClaims");
            return new SuccessDataResult<>(messageSource.getMessage("users.getSingleResult", new Object[]{user.getUsername()}, locale), jsonObject, 1L);
        }
    }

}
