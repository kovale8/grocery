package grocery;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Inventory {

    private static enum Header {

        Manufacturer(0),
        ProductName(1),
        Size(2),
        ItemType(3),
        Sku(4),
        BasePrice(5);

        private int column;

        Header(final int column) {
            this.column = column;
        }

        public int getColumn() {
            return column;
        }
    }

    // Information for parsing and rounding price.
    private static final NumberFormat CURRENCY_FORMAT =
        NumberFormat.getCurrencyInstance();
    private static final MathContext MATH_CONTEXT = new MathContext(3);

    // Information for products file
    private static final String DELIMITER = "\\|";
    private static final Charset ENCODING = StandardCharsets.ISO_8859_1;
    private static final Path PRODUCTS_FILE =
        Paths.get("resources", "products.txt");

    private final Map<String, List<Product>> products = new HashMap<>();
    private List<Product> miscProductsList;

    public Inventory(final BigDecimal salesPriceMultiplier) {
        try (final Stream<String> fileStream =
                // Skip the header line.
                Files.lines(PRODUCTS_FILE, ENCODING).skip(1)) {
            fileStream.forEach(line -> {
                final String[] values = line.split(DELIMITER);

                final List<Product> productTypeList;
                final String itemType =
                    values[Header.ItemType.getColumn()].toLowerCase();

                if (products.containsKey(itemType))
                    productTypeList = products.get(itemType);
                else {
                    productTypeList = new ArrayList<Product>();
                    products.put(itemType, productTypeList);
                }

                // Extract the raw price decimal from the currency string.
                String priceString = values[Header.BasePrice.getColumn()];
                try {
                    priceString = CURRENCY_FORMAT.parse(priceString).toString();
                }
                catch (ParseException ex) {
                    System.out.println(String.format(
                    "Could not parse currency: %s", priceString));
                }

                // Determine sales price rounded to two decimal places.
                BigDecimal price = new BigDecimal(priceString);
                price = price.multiply(salesPriceMultiplier, MATH_CONTEXT);

                productTypeList.add(new Product(
                    values[Header.Sku.getColumn()],
                    price
                ));
            });
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }

        miscProductsList = products
            .keySet()
            .stream()
            .filter(type -> !ConstraintType.contains(type))
            .map(type -> products.get(type))
            .flatMap(List::stream)
            .collect(Collectors.toList());
    }

    public Product getItem() {
        final int randIndex =
            ThreadLocalRandom.current().nextInt(miscProductsList.size());
        return miscProductsList.get(randIndex);
    }

    public Product getItem(final ConstraintType type) {
        final List<Product> productList = products.get(type.getName());
        final int randIndex =
            ThreadLocalRandom.current().nextInt(productList.size());
        return productList.get(randIndex);
    }
}
