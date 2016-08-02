package fedex.poc.roomer.model;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class Action {
    private String id;

    public Action(String id, String type, String untilTime) {
        this.id = id;
        this.type = type;
        this.untilTime = untilTime;
    }

    private String type;
    private String untilTime;

    public String getUntilTime() {
        return untilTime;
    }

    public String getType() {
        return type;
    }

    public String getId() {
        return id;
    }

    public static class Builder {
        private String id;
        private String type;
        private String untilTime;

        public Builder setId(String id) {
            this.id = id;
            return this;
        }

        public Builder setType(String type) {
            this.type = type;
            return this;
        }

        public Builder setUntilTime(String untilTime) {
            this.untilTime = untilTime;
            return this;
        }

        public Action createAction() {
            return new Action(id, type, untilTime);
        }
    }

}
