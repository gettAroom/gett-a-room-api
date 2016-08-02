package fedex.poc.roomer.conf;

import java.util.*;

/**
 * Created by tg60668 on 21/7/2016.
 */
public class RoomsMapper {

    private static final String EGOZI = "egozi";
    private static final String KIFKEF = "kifkef";
    private static final String BISLI = "bisli";
    private static final String DEMO = "demo";

    private static Map<String, List<String>> roomMap;


    static {
        roomMap = new HashMap<>();
        roomMap.put(EGOZI, Arrays.asList(KIFKEF, BISLI));
        roomMap.put(BISLI, Arrays.asList(KIFKEF, EGOZI));
        roomMap.put(KIFKEF, Arrays.asList(BISLI, EGOZI));
        roomMap.put(DEMO, Arrays.asList(EGOZI, KIFKEF, BISLI));
    }

    public static List<String> getFallbacks(String roomId) {
        return roomMap.getOrDefault(roomId, Collections.emptyList());
    }
}
