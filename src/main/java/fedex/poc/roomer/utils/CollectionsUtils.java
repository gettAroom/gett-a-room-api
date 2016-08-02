package fedex.poc.roomer.utils;

import java.util.Collection;

/**
 * Created by tg60668 on 27/7/2016.
 */
public class CollectionsUtils {
    
    public static boolean isEmpty(Collection<?> collection) {
        return collection == null || collection.isEmpty();
    }
}
