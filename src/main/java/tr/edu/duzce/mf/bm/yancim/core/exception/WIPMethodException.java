package tr.edu.duzce.mf.bm.yancim.core.exception;

public class WIPMethodException extends RuntimeException{
    public WIPMethodException() {
        super("Böyle bir metot bulunmamakta veya yetki seviyesi belirtilmemiş.");
    }
}
