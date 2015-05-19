package storage;

import com.opencsv.CSVWriter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.TimeZone;
import java.util.concurrent.BlockingQueue;

/**
 * User: YamStranger
 * Date: 4/15/15
 * Time: 4:18 PM
 */
public class CSVStorage extends Thread {
    public static Logger logger = LoggerFactory.getLogger(CSVStorage.class);
    private BlockingQueue<Item> results;
    private Path storage;
    private final boolean append;
    public static String separator = ";";
    public static String valueSeparator = "\"";


    public CSVStorage(Path file, BlockingQueue<Item> results, boolean append) {
        this.storage = file;
        this.results = results;
        this.append = append;
    }

    @Override
    public void run() {
        setName("Storage-" + getName());
        logger.info("user:started " + getName());
        /*init file*/
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        StringBuilder idBuilder = new StringBuilder();
        idBuilder.append(calendar.get(Calendar.MONTH) + 1).append(".");
        idBuilder.append(calendar.get(Calendar.DAY_OF_MONTH)).append(".");
        idBuilder.append(calendar.get(Calendar.YEAR)).append("_");
        idBuilder.append(calendar.get(Calendar.HOUR_OF_DAY)).append(".");
        idBuilder.append(calendar.get(Calendar.MINUTE)).append(".");
        idBuilder.append(calendar.get(Calendar.SECOND)).append(".");
        idBuilder.append(calendar.get(Calendar.MILLISECOND)).append(".");
        String fileId = idBuilder.toString();
        if (Files.exists(this.storage)) {
            if (Files.isDirectory(this.storage)) {
                storage = this.storage.resolve(fileId + this.storage.getFileName());
            } else {
                if (!append) {
                    storage = Paths.get(fileId + this.storage.getFileName());
                }
            }
        }


        try (CSVWriter csv = new CSVWriter(new OutputStreamWriter(
                new FileOutputStream(storage.toAbsolutePath().toFile(), true),
                Charset.forName("UTF-8").newEncoder()
        ))) {
            logger.info("user:Storage writes result into " + this.storage.toAbsolutePath());
            final LinkedList<String> line = new LinkedList();
            while (!isInterrupted()) {
                final Item current = results.take();
                logger.info("user: saved result for " + current.id());
                line.addAll(current.columns());
                csv.writeNext(line.toArray(new String[line.size()]), true);
                line.clear();
                csv.flush();
            }
        } catch (IOException e) {
            logger.error("processing storage finished by error", e);
        } catch (InterruptedException e) {
            this.interrupt();
            logger.error("Storage closed ", e);
            //exception handling left as an exercise for the reader
        }
    }
}
