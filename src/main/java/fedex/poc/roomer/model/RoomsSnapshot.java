package fedex.poc.roomer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.List;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class RoomsSnapshot implements Cacheable {

    public static final String SINGLE_ID = "1";

    public RoomsSnapshot(){}

    public Long getTimestamp() {
        return timestamp;
    }

    public List<Room> getRooms() {
        return rooms;
    }

    private Long timestamp;

    private List<Room> rooms;



    @JsonIgnore
    @Override
    public String getId() {
        return SINGLE_ID;
    }

    @JsonIgnore
    @Override
    public Long getVersion() {
        return this.timestamp;
    }
}
