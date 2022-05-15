package tr.edu.duzce.mf.bm.yancim.core.utilities.helper.converter;

import net.sf.json.JsonConfig;
import net.sf.json.processors.JsonValueProcessor;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

public class DateJSONValueProcessor implements JsonValueProcessor {

    private SimpleDateFormat simpleDateFormat;

    public DateJSONValueProcessor(String datePattern) {
        try {
            this.simpleDateFormat = new SimpleDateFormat(datePattern);
        } catch (Exception exception) {
            System.err.println("/16 DateJSONValueProcessor" + exception.getMessage());
        }
    }

    @Override
    public Object processArrayValue(Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    @Override
    public Object processObjectValue(String key, Object value, JsonConfig jsonConfig) {
        return process(value);
    }

    private Object process(Object value) {
        if (value != null) {
            if (value instanceof Timestamp)
                return simpleDateFormat.format((Timestamp) value);
            else
                return simpleDateFormat.format((Date) value);
        } else {
            return null;
        }
    }
}
