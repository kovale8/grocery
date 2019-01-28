package grocery;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.IOException;
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
import java.util.stream.Stream;

public class Inventory {

    private static final NumberFormat CURRENCY_FORMAT =
        NumberFormat.getCurrencyInstance();

    // Information for products file
    private static final String DELIMITER = "\\|";
    private static final Charset ENCODING = StandardCharsets.ISO_8859_1;
    private static final Path PRODUCTS_FILE = Paths.get("resources", "products.txt");

    // Product table columns
    private static final int MANUFACTURER = 0;
    private static final int PRODUCT_NAME = 1;
    private static final int SIZE = 2;
    private static final int ITEM_TYPE = 3;
    private static final int SKU = 4;
    private static final int BASE_PRICE = 5;

    private final Map<String, List<Product>> products = new HashMap<>();

    public Inventory() {
        try (final Stream<String> fileStream =
                // Skip the header line.
                Files.lines(PRODUCTS_FILE, ENCODING).skip(1)) {
            fileStream.forEach(line -> {
                final String[] cells = line.split(DELIMITER);

                final String itemType = cells[ITEM_TYPE].toLowerCase();
                final List<Product> productTypeList;

                if (products.get(itemType) != null)
                    productTypeList = products.get(itemType);
                else {
                    productTypeList = new ArrayList<Product>();
                    products.put(itemType, productTypeList);
                }

                productTypeList.add(new Product(
                    cells[MANUFACTURER],
                    cells[PRODUCT_NAME],
                    cells[SIZE],
                    cells[SKU],
                    parsePrice(cells[BASE_PRICE])
                ));
            });
        }
        catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public String getMilkSKU() {
        return products.get("milk").get(0).getSKU();
    }

    public void printTypes() {
        for (final String type : products.keySet())
            System.out.println(type);
    }

    private static double parsePrice(String priceString) {
        try {
            return CURRENCY_FORMAT.parse(priceString).doubleValue();
        }
        catch (ParseException ex) {
            System.out.println(String.format(
                "Could not parse currency: %s", priceString));
        }

        return Double.NaN;
    }
}
