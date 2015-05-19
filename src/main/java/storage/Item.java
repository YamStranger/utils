package storage;

import java.util.List;

/**
 * User: YamStranger
 * Date: 5/15/15
 * Time: 1:44 PM
 */
public interface Item {
    public List<String> columns();

    public String id();
}
