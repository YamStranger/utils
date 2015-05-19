package storage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * User: YamStranger
 * Date: 5/16/15
 * Time: 9:55 AM
 */
public class RowsReader implements Iterable<String> {
    private static final Logger logger = LoggerFactory.getLogger(RowsReader.class);
    private final Path file;
    private int read = 0;

    public RowsReader(Path file) {
        this.file = file;
    }

    @Override
    public Iterator<String> iterator() {
        return new IterableReaderIterator(this.file);
    }

    private class IterableReaderIterator implements Iterator<String> {
        private final Path file;
        private BufferedReader reader = null;
        private String line = null;
        private FileInputStream input;
        private Boolean hasNext = null;

        public IterableReaderIterator(Path file) {
            this.file = file;

        }

        private void init() throws IOException {
            if (this.reader == null || !input.getChannel().isOpen()) {
                if (input != null) {
                    try {
                        input.close();
                        input = null;
                    } catch (IOException e) {
                        logger.error("file closing error");
                    }
                }
                input = new FileInputStream(this.file.toAbsolutePath().toFile());
                this.reader = new BufferedReader(new InputStreamReader(input, Charset.forName("UTF-8")));
                int number = read;
                while (this.reader.readLine() != null && number-- > 0) {
                    this.reader.readLine();
                }
            }
        }


        @Override
        public boolean hasNext() {
            try {
                init();
                if (hasNext == null) {
                    line = reader.readLine();
                    if (line == null) {
                        this.reader.close();
                        hasNext = Boolean.FALSE;
                    } else {
                        hasNext = Boolean.TRUE;
                    }
                }
                return hasNext;
            } catch (IOException e) {
                logger.error("during reading exception" + e);
                try {
                    this.reader.close();
                } catch (Exception ee) {
                }
            }
            return false;
        }

        @Override
        public String next() {
            String copy = this.line;
            if (copy == null) {
                throw new UnsupportedOperationException("next called twice, or next does not exist");
            }
            read += 1;
            this.line = null;
            this.hasNext = null;
            return copy;
        }
    }
}
