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

    // Item probabilities
    // Milk
    private static final int MILK = 70;
    private static final int CEREAL_WITH_MILK = 50;
    private static final int CEREAL_NO_MILK = 5;
    // Baby food
    private static final int BABY_FOOD = 20;
    private static final int DIAPERS_WITH_BABY_FOOD = 80;
    private static final int DIAPERS_NO_BABY_FOOD = 1;
    // Bread
    private static final int BREAD = 50;
    // Peanut butter
    private static final int PEANUT_BUTTER = 10;
    private static final int JAM_WITH_PEANUT_BUTTER = 90;
    private static final int JAM_NO_PEANUT_BUTTER = 5;

    private static final Inventory inventory = new Inventory(PRICE_MULTIPLIER);
    private static final Date date = new Date(START_DATE);
    private static final Records records = new Records();

    private static int customerCount;
    private static int itemCount;

    public static void main(final String... args) {
        for (int day = 1; day <= DAYS_TO_RUN; day++, date.nextDay()) {
            int customerTotal = randRange(CUSTOMERS_LOW, CUSTOMERS_HIGH);
            if (date.isWeekend())
                customerTotal += WEEKEND_INCREASE;

            for (
                    customerCount = 1;
                    customerCount <= customerTotal;
                    customerCount++) {
                final int itemTotal = randRange(1, MAX_ITEMS);
                itemCount = 0;

                if (chance(MILK)) {
                    buyItem(ConstraintType.Milk);

                    if (chance(CEREAL_WITH_MILK))
                        buyItem(ConstraintType.Cereal);
                }
                else if (chance(CEREAL_NO_MILK))
                    buyItem(ConstraintType.Cereal);

                if (chance(BABY_FOOD)) {
                    buyItem(ConstraintType.BabyFood);

                    if (chance(DIAPERS_WITH_BABY_FOOD))
                        buyItem(ConstraintType.Diapers);
                }
                else if (chance(DIAPERS_NO_BABY_FOOD))
                    buyItem(ConstraintType.Diapers);

                if (chance(BREAD))
                    buyItem(ConstraintType.Bread);

                if (chance(PEANUT_BUTTER)) {
                    buyItem(ConstraintType.PeanutButter);

                    if (chance(JAM_WITH_PEANUT_BUTTER))
                        buyItem(ConstraintType.Jam);
                }
                else if (chance(JAM_NO_PEANUT_BUTTER))
                    buyItem(ConstraintType.Jam);

                // Buy random products to fill item total.
                while (itemCount <= itemTotal)
                    buyItem();
            }
        }
    }

    private static void buyItem() {
        recordSale(inventory.getItem());
    }

    private static void buyItem(final ConstraintType type) {
        recordSale(inventory.getItem(type.toString()));
    }

    private static boolean chance(final int probability) {
        return randRange(1, 100) <= probability;
    }

    private static int randRange(final int low, final int high) {
        return ThreadLocalRandom.current().nextInt(low, high + 1);
    }

    private static void recordSale(final Product product) {
        itemCount++;

        records.writeRecord(
            date.toString(),
            Integer.toString(customerCount),
            product.getSKU(),
            product.getPrice().toString()
        );
    }
}
