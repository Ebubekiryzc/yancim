package tr.edu.duzce.mf.bm.yancim.core.utilities.result;

public class SuccessDataResult<TEntity> extends DataResult<TEntity> {
    public SuccessDataResult() {
        super(true, null, 0L);
    }

    public SuccessDataResult(String message) {
        super(true, message, null, 0L);
    }

    public SuccessDataResult(TEntity data, Long totalDataCount) {
        super(true, data, totalDataCount);
    }

    public SuccessDataResult(String message, TEntity data, Long totalDataCount) {
        super(true, message, data, totalDataCount);
    }
}
