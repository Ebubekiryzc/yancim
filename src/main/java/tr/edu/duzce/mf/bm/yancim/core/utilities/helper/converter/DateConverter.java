package tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter;

import java.text.SimpleDateFormat;

public class DateConverter {
    private SimpleDateFormat simpleDateFormat;

    public DateConverter(String pattern) {
        try {
            this.simpleDateFormat = new SimpleDateFormat(pattern);
        } catch (Exception exception) {
            System.err.println("/12 DateConverter" + exception.getMessage());
        }
    }


}
