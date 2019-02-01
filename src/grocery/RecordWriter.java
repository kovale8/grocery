package grocery;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RecordWriter {

    private static final String DELIMITER = "|";

    private BufferedWriter writer;
    private Path target;

    public RecordWriter(final String outFilename) {
        final Path homedir = Paths.get(System.getProperty("user.home"));
        final Path desktop = homedir.resolve("Desktop");

        if (Files.isDirectory(desktop))
            target = desktop.resolve(outFilename);
        else
            target = desktop.resolve(outFilename);

        try {
            writer = Files.newBufferedWriter(target);
        }
        catch (IOException ex) {
            System.out.println(String.format(
                "Problem opening or creating file: %s", target));
            ex.printStackTrace();
            System.exit(1);
        }
    }

    public void close() {
        try {
            writer.close();
        }
        catch (IOException ex) {
            System.out.println(String.format(
                "Problem closing file: %s", target));
            ex.printStackTrace();
        }
    }

    public void writeRecord(final String... values) {
        final String record = String.join(DELIMITER, values);

        try {
            writer.write(record);
            writer.newLine();
        }
        catch (IOException ex) {
            System.out.println(String.format(
                "Problem writing to file: %s", target));
            ex.printStackTrace();
            System.exit(1);
        }
    }
}
