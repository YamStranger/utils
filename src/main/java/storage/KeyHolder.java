package storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;

/**
 * User: YamStranger
 * Date: 5/15/15
 * Time: 1:54 PM
 */
public class KeyHolder {
    private static final Logger logger = LoggerFactory.getLogger(KeyHolder.class);
    private final Set<String> hash = new HashSet();
    private final Path storage;

    public KeyHolder(Path path) {
        this.storage = path;
    }

    public void register(String key) {
        if (this.hash.isEmpty()) {
            init();
        }
        this.hash.add(key);
        try {
            if (!Files.exists(this.storage)) {
                Files.createFile(this.storage);
            }
        } catch (IOException e) {
            logger.error("Storage error, can not create storage, cant write used values ", e);
        }
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(storage.toAbsolutePath().toFile(), true),
                Charset.forName("UTF-8").newEncoder()
        ))) {
            writer.write(key);
            writer.newLine();
            writer.flush();
        } catch (IOException e) {
            logger.error("Storage error, cant write used values ", e);
        }
    }

    /**
     * return true - if already processed.
     */
    public boolean contains(String key) {
        if (hash.isEmpty()) {
            init();
        }

        //check and update
        return this.hash.contains(key);
    }

    private void init() {
        //load
        if (Files.exists(storage)) {
            try (BufferedReader reader = new BufferedReader(new InputStreamReader(
                    new FileInputStream(this.storage.toAbsolutePath().toFile()),
                    Charset.forName("UTF-8")))) {
                String line = null;
                while ((line = reader.readLine()) != null) {
                    this.hash.add(line);
                }
            } catch (IOException e) {
                logger.error("Storage error, cant check if values used before" + e);
            }
        }
    }

    public List<String> values() {
        if (hash.isEmpty()) {
            init();
        }
        return new ArrayList<>(this.hash);
    }

}
