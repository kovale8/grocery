package grocery;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Inventory {

    private static final int DEFAULT_STOCK_TARGET = 151623;
    private static final List<Integer> DELIVERY_DAYS = Arrays.asList(
        Calendar.TUESDAY,
        Calendar.THURSDAY,
        Calendar.SATURDAY
    );

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
        restockAll();
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

    public void restock(final Date date) {
        final String milk = ConstraintType.Milk.getName();
        restockByType(milk);

        if (DELIVERY_DAYS.contains(date.getDayOfWeek()))
            for (final String type : products.keySet())
                if (!type.equals(milk))
                    restockByType(type);
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

            productTypeList.add(new Product(
                record.get(SKU),
                itemType,
                salesPrice
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

    private int getStockTarget(final String itemType) {
        int stockTarget;
        int productCount;

        if (ConstraintType.contains(itemType)) {
            stockTarget = ConstraintType.find(itemType).getStockTarget();
            productCount = products.get(itemType).size();
        }
        else {
            stockTarget = DEFAULT_STOCK_TARGET;
            productCount = miscProductsList.size();
        }

        return stockTarget / productCount;
    }

    private void restockAll() {
        for (final String type : products.keySet())
            restockByType(type);
    }

    private void restockByType(final String itemType) {
        final List<Product> productList = products.get(itemType);
        final int stockTarget = getStockTarget(itemType);

        for (final Product product : productList)
            product.restock(stockTarget);
    }
}
