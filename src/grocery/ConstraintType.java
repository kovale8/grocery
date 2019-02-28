package grocery;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public enum ConstraintType {

    Milk("milk", 1197),
    Cereal("cereal", 1248),
    BabyFood("baby food", 681),
    Diapers("diapers", 570),
    Bread("bread", 1710),
    PeanutButter("peanut butter", 342),
    Jam("jelly/jam", 462);

    private static final List<String> typeList;
    private static final Map<String, ConstraintType> typeMap = new HashMap<>();

    static {
        final List<String> values = Arrays
            .asList(values())
            .stream()
            .map(t -> t.typeName)
            .collect(Collectors.toList());
        Collections.sort(values);
        typeList = Collections.unmodifiableList(values);

        for (final ConstraintType type : values())
            typeMap.put(type.getName(), type);
    }

    public static boolean contains(final String searchKey) {
        return Collections.binarySearch(typeList, searchKey) >= 0;
    }

    public static ConstraintType find(final String searchKey) {
        return typeMap.get(searchKey);
    }

    private final String typeName;
    private final int stockTarget;

    ConstraintType(final String typeName, final int stockTarget) {
        this.typeName = typeName;
        this.stockTarget = stockTarget;
    }

    public String getName() {
        return typeName;
    }

    public int getStockTarget() {
        return stockTarget;
    }
}
