package grocery;

public class Product {

    public static final int ITEMS_PER_CASE = 12;

    private final String sku;
    private final String type;
    private final Cost price;
    private int casesOrdered = 0;
    private int stock = 0;

    public Product(final String sku, final String type, final Cost price) {
        this.sku = sku;
        this.type = type;
        this.price = price;
    }

    public int getCasesOrdered() {
        return casesOrdered;
    }

    public int getStock() {
        return stock;
    }

    public Cost getPrice() {
        return price;
    }

    public String getSKU() {
        return sku;
    }

    public String getType() {
        return type;
    }

    public boolean isInStock() {
        return stock > 0;
    }

    public void purchase() {
        if (isInStock()) stock--;
        else {
            System.out.println(String.format(
                "Product out of stock: (SKU: %s)", sku));
            System.exit(1);
        }
    }

    public void restock(final int stockTarget) {
        final int countToOrder = stockTarget - stock;
        if (countToOrder <= 0) return;

        int cases = countToOrder / ITEMS_PER_CASE;
        if (countToOrder % ITEMS_PER_CASE != 0) cases++;

        stock += cases * ITEMS_PER_CASE;
        casesOrdered += cases;
    }
}
