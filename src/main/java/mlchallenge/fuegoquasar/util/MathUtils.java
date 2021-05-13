package mlchallenge.fuegoquasar.util;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class MathUtils {
    private MathUtils() {
    }

    public static float roundTwoDecimals(float value) {
        final BigDecimal bd = new BigDecimal(value).setScale(2, RoundingMode.HALF_UP);
        return bd.floatValue();
    }

    public static int roundUpInt(double value) {
        final BigDecimal bd = new BigDecimal(value).setScale(0, RoundingMode.UP);
        return bd.intValue();
    }

    public static float powerTwo(float value) {
        return value * value;
    }

}
