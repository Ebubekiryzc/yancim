package tr.edu.duzce.mf.bm.yancim.core.exception;

public class NotAllowedException extends RuntimeException {
    public NotAllowedException() {
        super("Bu işlemi gerçekleştirmek için yetkiniz yok.");
    }
}
