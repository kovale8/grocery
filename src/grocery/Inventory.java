package grocery;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

    // TODO Change this placeholder value.
    private static final int DEFAULT_STOCK_TARGET = 100;

    // Information for products file
    private static final String PRODUCTS_FILENAME = "products.txt";

    // Relevant product table headers
    private static final String BASE_PRICE = "base_price";
    private static final String ITEM_TYPE = "item_type";
    private static final String SKU = "sku";

    // Mapping of all products by item type
    private final Map<String, List<Product>> products = new HashMap<>();
    // List of all products not of the types represented by ConstraintType
    private List<Product> miscProductsList;
    // Used to calculate product sales price from base price
    private BigDecimal salesPriceMultiplier;

    public Inventory(final String salesPriceMultiplier) {
        this.salesPriceMultiplier = new BigDecimal(salesPriceMultiplier);

        buildInventoryFromFile();
        populateMiscProducts();
    }

    public Product getItem() {
        return Random.randomElement(miscProductsList);
    }

    public Product getItem(final ConstraintType type) {
        return Random.randomElement(products.get(type.getName()));
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

    private void populateMiscProducts() {
        miscProductsList = products
            .keySet()
            .stream()
            .filter(type -> !ConstraintType.contains(type))
            .flatMap(type -> products.get(type).stream())
            .collect(Collectors.toList());
    }
}
