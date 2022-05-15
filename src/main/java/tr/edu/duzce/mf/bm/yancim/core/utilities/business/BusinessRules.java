package tr.edu.duzce.mf.bm.yancim.core.utilities.business;

import tr.edu.duzce.mf.bm.yancim.core.utilities.result.Result;
import tr.edu.duzce.mf.bm.yancim.core.utilities.result.SuccessResult;

public class BusinessRules {
    public static Result check(Result... rules) {
        for (Result rule : rules) {
            if (!rule.isSuccess()) {
                return rule;
            }
        }
        return new SuccessResult();
    }
}
