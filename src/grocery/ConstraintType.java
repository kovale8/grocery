package grocery;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public enum ConstraintType {

    Milk("milk"),
    Cereal("cereal"),
    BabyFood("baby food"),
    Diapers("diapers"),
    Bread("bread"),
    PeanutButter("peanut butter"),
    Jam("jelly/jam");

    private static final List<String> typeList;

    static {
        final List<String> values = Arrays
            .asList(values())
            .stream()
            .map(t -> t.typeName)
            .collect(Collectors.toList());
        Collections.sort(values);
        typeList = Collections.unmodifiableList(values);
    }

    public static boolean contains(final String searchKey) {
        return Collections.binarySearch(typeList, searchKey) >= 0;
    }

    private String typeName;

    ConstraintType(final String typeName) {
        this.typeName = typeName;
    }

    public String getName() {
        return typeName;
    }
}
