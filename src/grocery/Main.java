package grocery;

import java.util.Optional;

public class Main {

    // Customer properties
    private static final int CUSTOMERS_LOW = 1100;
    private static final int CUSTOMERS_HIGH = 1150;
    private static final int WEEKEND_INCREASE = 50;
    private static final int MAX_ITEMS = 90;

    // Product properties
    private static final String PRICE_MULTIPLIER = "1.07";

    // Date properties
    private static final String START_DATE = "20170101";
    private static final int DAYS_TO_RUN = 365;

    // User properties
    private static final String OUTPUT_FILE = "sales.txt";

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
    private static final RecordWriter records = new RecordWriter(OUTPUT_FILE);

    private static int customerCount;
    private static int itemCount;

    public static void main(final String... args) {
        for (int day = 1; day <= DAYS_TO_RUN; day++, date.nextDay()) {
            inventory.restock(date);

            int customerTotal = Random.randRange(CUSTOMERS_LOW, CUSTOMERS_HIGH);
            if (date.isWeekend())
                customerTotal += WEEKEND_INCREASE;

            for (
                    customerCount = 1;
                    customerCount <= customerTotal;
                    customerCount++) {
                final int itemTotal = Random.randRange(1, MAX_ITEMS);
                itemCount = 0;

                // Handle item probability constraints.
                if (Random.chance(MILK)) {
                    buyItem(ConstraintType.Milk);

                    if (Random.chance(CEREAL_WITH_MILK))
                        buyItem(ConstraintType.Cereal);
                }
                else if (Random.chance(CEREAL_NO_MILK))
                    buyItem(ConstraintType.Cereal);

                if (Random.chance(BABY_FOOD)) {
                    buyItem(ConstraintType.BabyFood);

                    if (Random.chance(DIAPERS_WITH_BABY_FOOD))
                        buyItem(ConstraintType.Diapers);
                }
                else if (Random.chance(DIAPERS_NO_BABY_FOOD))
                    buyItem(ConstraintType.Diapers);

                if (Random.chance(BREAD))
                    buyItem(ConstraintType.Bread);

                if (Random.chance(PEANUT_BUTTER)) {
                    buyItem(ConstraintType.PeanutButter);

                    if (Random.chance(JAM_WITH_PEANUT_BUTTER))
                        buyItem(ConstraintType.Jam);
                }
                else if (Random.chance(JAM_NO_PEANUT_BUTTER))
                    buyItem(ConstraintType.Jam);

                // Buy random products to fill item total.
                while (itemCount <= itemTotal)
                    buyItem();
            }
        }

        records.close();
    }

    private static void buyItem() {
        Optional<Product> optProduct;

        do {
            optProduct = inventory.getItem();
        } while (!optProduct.isPresent());

        recordSale(optProduct.get());
    }

    private static void buyItem(final ConstraintType type) {
        inventory.getItem(type).ifPresent(product -> recordSale(product));
    }

    private static void recordSale(final Product product) {
        product.purchase();
        itemCount++;

        records.writeRecord(
            date,
            customerCount,
            product.getSKU(),
            product.getPrice(),
            product.getStock(),
            product.getCasesOrdered()
        );
    }
}
