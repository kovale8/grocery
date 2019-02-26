package grocery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

    private static final int DEFAULT_STOCK_TARGET = 1680;

    // Information for products file
    private static final String PRODUCTS_FILENAME = "products.txt";

    // Relevant product table headers
    private static final String BASE_PRICE = "base_price";
    private static final String ITEM_TYPE = "item_type";
    private static final String SKU = "sku";

    // Mapping of all products by item type
    private final Map<String, List<Product>> products = new HashMap<>();
    // List of all products not of the types represented by ConstraintType
    private final List<Product> miscProductsList;
    // Used to calculate product sales price from base price
    private final BigDecimal salesPriceMultiplier;

    public Inventory(final String salesPriceMultiplier) {
        this.salesPriceMultiplier = new BigDecimal(salesPriceMultiplier);

        buildInventoryFromFile();
        miscProductsList = getMiscProducts();
    }

    public Product getItem() {
        final Product randomProduct = Random.randomElement(miscProductsList);

        if (randomProduct.isInStock())
            return randomProduct;

        return getItem(randomProduct.getType());
    }

    public Product getItem(final ConstraintType type) {
        return getItem(type.getName());
    }

    private void buildInventoryFromFile() {
        RecordReader.streamRecords(PRODUCTS_FILENAME).forEach(record -> {
            final List<Product> productTypeList;
            final String itemType = record.get(ITEM_TYPE).toLowerCase();

            if (products.containsKey(itemType))
                productTypeList = products.get(itemType);
            else {
                productTypeList = new ArrayList<Product>();
                products.put(itemType, productTypeList);
            }

            final Cost salesPrice = new Cost(record.get(BASE_PRICE))
                .multiply(salesPriceMultiplier);

            final int stockTarget = ConstraintType.contains(itemType) ?
                ConstraintType.find(itemType).getStockTarget() :
                DEFAULT_STOCK_TARGET;

            productTypeList.add(new Product(
                record.get(SKU),
                itemType,
                salesPrice,
                stockTarget
            ));
        });
    }

    private Product getItem(final String type) {
        final List<Product> productsOfType = products
            .get(type)
            .stream()
            .filter(p -> p.isInStock())
            .collect(Collectors.toList());

        if (productsOfType.isEmpty()) {
            System.out.println(String.format(
                "Products of type '%s' are out of stock", type));
            System.exit(1);
        }

        return Random.randomElement(productsOfType);
    }

    private List<Product> getMiscProducts() {
        return products
            .keySet()
            .stream()
            .filter(type -> !ConstraintType.contains(type))
            .flatMap(type -> products.get(type).stream())
            .collect(Collectors.toList());
    }
}
