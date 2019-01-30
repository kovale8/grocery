package grocery;

public class Main {

    private static final int CUSTOMERS_LOW = 1100;
    private static final int CUSTOMERS_HIGH = 1150;
    private static final double PRICE_MULTIPLIER = 1.07;
    private static final String START_DATE = "2017-01-01";
    private static final int MAX_ITEMS = 90;
    private static final int WEEKEND_INCREASE = 50;

    public static void main(String... args) {
        Inventory inventory = new Inventory();
        System.out.println(inventory.getSKU("milk"));
    }
}
