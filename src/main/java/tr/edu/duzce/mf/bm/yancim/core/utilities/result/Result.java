package tr.edu.duzce.mf.bm.yancim.core.utilities.result;

import lombok.Getter;

@Getter
public class Result {
    private boolean success;
    private String message;

    public Result(boolean success) {
        this.success = success;
    }

    public Result(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
}
