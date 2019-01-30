package grocery;

import java.math.BigDecimal;
import java.util.concurrent.ThreadLocalRandom;

public class Main {

    private static final int CUSTOMERS_LOW = 1100;
    private static final int CUSTOMERS_HIGH = 1150;
    private static final BigDecimal PRICE_MULTIPLIER = new BigDecimal("1.07");
    private static final String START_DATE = "20170101";
    private static final int MAX_ITEMS = 90;
    private static final int WEEKEND_INCREASE = 50;
    private static final int DAYS_TO_RUN = 1;

    public static void main(final String... args) {
        final Inventory inventory = new Inventory(PRICE_MULTIPLIER);
        final Date date = new Date(START_DATE);
        final Records records = new Records();

        for (int day = 1; day <= DAYS_TO_RUN; day++, date.nextDay()) {
            int customerCount = randRange(CUSTOMERS_LOW, CUSTOMERS_HIGH);
            if (date.isWeekend())
                customerCount += WEEKEND_INCREASE;

            for (int customer = 1; customer <= customerCount; customer++) {
                final int itemTotal = randRange(1, MAX_ITEMS);

                for (int itemCount = 0; itemCount <= itemTotal; itemCount++) {
                    Product item = inventory.getItem();

                    records.writeRecord(
                        date.toString(),
                        Integer.toString(customer),
                        item.getSKU(),
                        item.getPrice().toString()
                    );
                }
            }
        }
    }

    private static int randRange(final int low, final int high) {
        return ThreadLocalRandom.current().nextInt(low, high + 1);
    }
}
