package grocery;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class Random {

    public static boolean chance(final int probability) {
        return randRange(1, 100) <= probability;
    }

    public static <T> T randomElement(final List<T> list) {
        final int randIndex = generator().nextInt(list.size());
        return list.get(randIndex);
    }

    public static int randRange(final int low, final int high) {
        return generator().nextInt(low, high + 1);
    }

    private static ThreadLocalRandom generator() {
        return ThreadLocalRandom.current();
    }
}
