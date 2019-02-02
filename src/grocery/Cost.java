package grocery;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.NumberFormat;
import java.text.ParseException;

public class Cost {

    private static final int PRECISION = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    private static String parse(String priceString) {
        try {
            priceString = NumberFormat
                .getCurrencyInstance()
                .parse(priceString)
                .toString();
        }
        catch (ParseException ex) {
            System.out.println(String.format(
                "Could not parse currency: %s", priceString));
            System.exit(1);
        }

        return priceString;
    }

    private BigDecimal value;

    public Cost(final BigDecimal value) {
        this.value = value.setScale(PRECISION, ROUNDING_MODE);
    }

    public Cost(final String priceString) {
        this(new BigDecimal(parse(priceString)));
    }

    public Cost multiply(final BigDecimal multiplier) {
        return new Cost(value.multiply(multiplier));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
