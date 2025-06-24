import java.util.ArrayList;
import java.util.List;

public class CollectionUtils {

    public static <T extends Comparable<T>> T max(List<T> list) {
        if (list == null) {
            throw new NullPointerException("list cannot be null!");
        }
        if (list.isEmpty()) {
            throw new IllegalArgumentException("list cannot be empty");
        }

        // assume the first element is the max element
        T maxItem = list.get(0);
        // go over all the elements in the list
        for (T item : list) {
            if (item == null) {
                throw new IllegalArgumentException("list cannot contain null items");
            }
            // if the current max is smaller than some other element 'e'
            if (maxItem.compareTo(item) < 0) {
                // we set 'e' as the new max
                maxItem = item;
            }
        }
        return maxItem;
    }

    public static <T> List<T> findDuplicates(List<T> list) {
        if (list == null) {
            throw new NullPointerException("list cannot be null!");
        }
        if (list.isEmpty()) {
            return new ArrayList<>();
        }

        List<T> duplicates = new ArrayList<>();
        T firstItem = null, secondItem = null;
        // no time complexity requirements were given in the assignment, so iterate over all the elements
        // and compare every element to every element except for himself
        for (int i = 0; i < list.size(); i++) {
            firstItem = list.get(i);
            if (firstItem == null) {
                // skip when comparing nulls, to stay consistent with the java.lang.Object javadoc :
                // For any non-null reference value x, x.equals(null) should return false.
                continue;
            }
            for (int j = 0; j < list.size(); j++) {
                secondItem = list.get(j);
                if (secondItem == null) {
                    // skip when comparing nulls, to stay consistent with the java.lang.Object javadoc :
                    // For any non-null reference value x, x.equals(null) should return false.
                    continue;
                }
                if (j == i) {
                    // skip when comparing an element to itself
                    continue;
                }
                if (firstItem.equals(secondItem) && !duplicates.contains(firstItem)) {
                    // make sure to only add an item to the duplicates list once!
                    duplicates.add(firstItem);
                }
            }
        }
        return duplicates;
    }
}
