package grocery;

public class Product {

    private String manufacturer;
    private String name;
    private String size;
    private String sku;
    private double basePrice;

    public Product(
            String manufacturer,
            String name,
            String size,
            String sku,
            double basePrice) {
        this.manufacturer = manufacturer;
        this.name = name;
        this.size = size;
        this.sku = sku;
        this.basePrice = basePrice;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public String getName() {
        return name;
    }

    public String getSize() {
        return size;
    }

    public String getSKU() {
        return sku;
    }

    public double basePrice() {
        return basePrice;
    }
}
