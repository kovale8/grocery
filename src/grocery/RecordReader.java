package grocery;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

public class RecordReader {

    public static class Record {

        private final Map<String, String> rowValues = new HashMap<>();

        public Record(final String[] headers, final String[] values) {
            for (int i = 0; i < headers.length; i++)
                rowValues.put(headers[i], values[i]);
        }

        public String get(final String header) {
            if (!rowValues.containsKey(header)) {
                System.out.println(String.format(
                    "Column name not found: %s", header));
                System.exit(1);
            }

            return rowValues.get(header);
        }
    }

    private static final String DELIMITER = "\\|";
    private static final Charset ENCODING = StandardCharsets.ISO_8859_1;
    private static final Path RESOURCES = Paths.get("resources");

    public static Stream<Record> streamRecords(final String filename) {
        final Path file = RESOURCES.resolve(filename);

        try {
            final List<String> lines = Files.readAllLines(file, ENCODING);
            final String[] headers = lines.get(0).split(DELIMITER);

            return lines
                .stream()
                .skip(1)
                .map(line -> new Record(headers, line.split(DELIMITER)));
        }
        catch (IOException ex) {
            System.out.println(String.format(
                "Problem reading file: %s", file));
            ex.printStackTrace();
            System.exit(1);
        }

        return Stream.empty();
    }
}
