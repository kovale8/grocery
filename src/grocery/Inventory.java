package grocery;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

    // Information for products file
    private static final String PRODUCTS_FILENAME = "products.txt";

    // Round prices to two decimal places.
    private static final int PRICE_PRECISION = 3;

    // Relevant product table headers
    private static final String BASE_PRICE = "Base Price";
    private static final String ITEM_TYPE = "Item Type";
    private static final String SKU = "SKU";

    // Mapping of all products by item type
    private final Map<String, List<Product>> products = new HashMap<>();
    // List of all products not of the types represented by ConstraintType
    private List<Product> miscProductsList;
    // Used to calculate product sales price from base price
    private BigDecimal salesPriceMultiplier;

    public Inventory(final BigDecimal salesPriceMultiplier) {
        this.salesPriceMultiplier = salesPriceMultiplier;

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

            // Extract the raw price decimal from the currency string.
            String priceString = record.get(BASE_PRICE);
            try {
                priceString = NumberFormat
                    .getCurrencyInstance()
                    .parse(priceString)
                    .toString();
            }
            catch (ParseException ex) {
                System.out.println(String.format(
                    "Could not parse currency: %s", priceString));
                ex.printStackTrace();
                System.exit(1);
            }

            // Determine sales price rounded to two decimal places.
            final BigDecimal price = new BigDecimal(priceString).multiply(
                salesPriceMultiplier, new MathContext(PRICE_PRECISION));

            productTypeList.add(new Product(record.get(SKU), price));
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
