package grocery;

public class Product {

    public static final int ITEMS_PER_CASE = 12;

    private final String sku;
    private final String type;
    private final Cost price;
    private int casesOrdered;
    private int stock;

    public Product(final String sku, final String type, final Cost price,
            final int stockTarget) {
        this.sku = sku;
        this.type = type;
        this.price = price;

        casesOrdered = stockTarget / ITEMS_PER_CASE;
        if (stockTarget % ITEMS_PER_CASE != 0)
            casesOrdered++;
        stock = casesOrdered * ITEMS_PER_CASE;
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
        if (isInStock())
            stock--;
        else {
            System.out.println(String.format(
                "Product out of stock: (SKU: %s)", sku));
            System.exit(1);
        }
    }
}
