package grocery;

public class Product {

    private String sku;
    private Cost price;

    public Product(final String sku, final Cost price) {
        this.sku = sku;
        this.price = price;
    }

    public String getSKU() {
        return sku;
    }

    public Cost getPrice() {
        return price;
    }
}
