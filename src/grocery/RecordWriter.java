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

    public RecordWriter(final String filename) {
        final Path homedir = Paths.get(System.getProperty("user.home"));
        final Path desktop = homedir.resolve("Desktop");

        if (Files.isDirectory(desktop))
            target = desktop.resolve(filename);
        else
            target = homedir.resolve(filename);

        try {
            writer = Files.newBufferedWriter(target);
        }
        catch (IOException ex) {
            printError("Problem opening or creating file", ex);
        }
    }

    public void close() {
        try {
            writer.close();
        }
        catch (IOException ex) {
            printError("Problem closing file", ex);
        }
    }

    public void writeRecord(final String... values) {
        try {
            writer.write(String.join(DELIMITER, values));
            writer.newLine();
        }
        catch (IOException ex) {
            printError("Problem writing to file", ex);
        }
    }

    private void printError(final String issue, final Exception ex) {
        System.out.println(String.format("%s: %s", issue, target));
        ex.printStackTrace();
        System.exit(1);
    }
}
