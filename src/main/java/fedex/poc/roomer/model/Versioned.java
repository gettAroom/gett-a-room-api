package fedex.poc.roomer.model;

/**
 * Created by tg60668 on 20/7/2016.
 */
public interface Versioned {
    Long getVersion();

    default boolean isNewer(Versioned other) {
        return this.getVersion() > other.getVersion();
    }
}
