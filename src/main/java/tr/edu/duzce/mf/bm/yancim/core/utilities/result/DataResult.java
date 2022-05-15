package tr.edu.duzce.mf.bm.yancim.core.utilities.result;

import lombok.Getter;

@Getter
public class DataResult<TEntity> extends Result {
    private TEntity data;
    private Long totalDataCount;

    public DataResult(boolean success, TEntity data, Long totalDataCount) {
        super(success);
        this.data = data;
        this.totalDataCount = totalDataCount;
    }

    public DataResult(boolean success, String message, TEntity data, Long totalDataCount) {
        super(success, message);
        this.data = data;
        this.totalDataCount = totalDataCount;
    }
}
