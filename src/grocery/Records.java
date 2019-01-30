package grocery;

public class Records {

    public static final String DELIMITER = "|";

    public void writeRecord(final String... values) {
        final String record = String.join(DELIMITER, values);

        // TODO
        System.out.println(record);
    }
}
