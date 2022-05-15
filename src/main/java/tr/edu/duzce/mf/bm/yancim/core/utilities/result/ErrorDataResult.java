package tr.edu.duzce.mf.bm.yancim.core.utilities.result;

public class ErrorDataResult<TEntity> extends DataResult<TEntity> {
    public ErrorDataResult() {
        super(true, null, 0L);
    }

    public ErrorDataResult(String message) {
        super(false, message, null, 0L);
    }

    public ErrorDataResult(TEntity data, Long totalDataCount) {
        super(false, data, totalDataCount);
    }

    public ErrorDataResult(String message, TEntity data, Long totalDataCount) {
        super(false, message, data, totalDataCount);
    }
}
