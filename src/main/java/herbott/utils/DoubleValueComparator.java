package herbott.utils;

import java.util.Comparator;
import java.util.Map;

public class DoubleValueComparator implements Comparator<String> {

    private Map<String, Double> base;

    public DoubleValueComparator(Map<String, Double> base) {
        this.base = base;
    }

    // Note: this comparator imposes orderings that are inconsistent with
    // equals.

    @Override
    @SuppressWarnings("ComparatorMethodParameterNotUsed")
    public int compare(String a, String b) {
        if (base.get(a) >= base.get(b)) {
            return -1;
        } else {
            return 1;
        } // returning 0 would merge keys
    }
}
