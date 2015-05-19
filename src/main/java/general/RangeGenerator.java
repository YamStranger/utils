package general;

import java.util.LinkedList;
import java.util.List;

/**
 * User: YamStranger
 * Date: 5/16/15
 * Time: 10:53 AM
 */
public class RangeGenerator {
    private final int level;
    private final int increase;

    public RangeGenerator(int level, int increase) {
        this.level = level;
        this.increase = increase;
    }

    public List<Pair<Integer, Integer>> generate(int start, int end, int step) {
        List<Pair<Integer, Integer>> result = new LinkedList<>();
        int levelCount = 0;
        for (int i = start; i <= end; i += step) {
            levelCount+=step;
            if (levelCount >= level) {
                step += increase;
                levelCount = 0;
            }
            if (i + step -1 <= end) {
                result.add(new Pair(i, i + step - 1));
            } else {
                result.add(new Pair(i, end));
            }
        }
        return result;
    }
}
