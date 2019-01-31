package grocery;

import java.math.BigDecimal;

public class Product {

    private String sku;
    private BigDecimal price;

    public Product(final String sku, final BigDecimal price) {
        this.sku = sku;
        this.price = price;
    }

    public String getSKU() {
        return sku;
    }

    public BigDecimal getPrice() {
        return price;
    }
}
