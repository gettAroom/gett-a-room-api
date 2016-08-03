package fedex.poc.roomer.api.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import fedex.poc.roomer.model.Meeting;
import fedex.poc.roomer.model.Room;
import fedex.poc.roomer.services.RoomAvailabilityService;

import java.util.List;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class RoomRep {
    private String id;
    private String name;
    private boolean isAvailable = false;
    private Integer steps;
    private MeetingRep nextMeeting;
    private List<RoomRep> fallbacks;

    public RoomRep() {}

    public RoomRep(String id, String name, boolean isAvailable, Integer steps, MeetingRep nextMeeting) {
        this.id = id;
        this.name = name;
        this.isAvailable = isAvailable;
        this.steps = steps;
        this.nextMeeting = nextMeeting;
    }

    public static RoomRep fromRoom(Room room, RoomAvailabilityService roomAvailabilityService) {
        RoomRep roomRep = new RoomRep();
        roomRep.setId(room.id);
        roomRep.setName(room.name);
        roomRep.setSteps(roomAvailabilityService.getRoomStepsMinutes(room));
        roomRep.setNextMeeting(new MeetingRep(room.nextMeeting));
        roomRep.setIsAvailable(roomAvailabilityService.isRoomAvailable(room));
        return roomRep;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public Integer getSteps() {
        return steps;
    }

    public MeetingRep getNextMeeting() {
        return nextMeeting;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setIsAvailable(boolean isAvailable) {
        this.isAvailable = isAvailable;
    }

    public void setSteps(Integer steps) {
        this.steps = steps;
    }

    public void setNextMeeting(MeetingRep nextMeeting) {
        this.nextMeeting = nextMeeting;
    }

    public List<RoomRep> getFallbacks() {
        return fallbacks;
    }

    public void setFallbacks(List<RoomRep> fallbacks) {
        this.fallbacks = fallbacks;
    }

    public static class MeetingRep {
        private String from;
        private String to;
        private String orgenizer;
        private String subject;

        public MeetingRep(String from, String to, String orgenizer, String subject, String phone) {
            this.from = from;
            this.to = to;
            this.orgenizer = orgenizer;
            this.subject = subject;
            this.phone = phone;
        }

        private String phone;

        public MeetingRep(Meeting meeting) {
            if (meeting != null) {
                this.from = meeting.startTime;
                this.to = meeting.endTime;
                this.orgenizer = meeting.orgenizer;
                this.subject = meeting.subject;
                this.phone = meeting.phone;
            }
        }

        public MeetingRep() {
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getOrgenizer() {
            return orgenizer;
        }

        public void setOrgenizer(String orgenizer) {
            this.orgenizer = orgenizer;
        }

        public String getSubject() {
            return subject;
        }

        public void setSubject(String subject) {
            this.subject = subject;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        @JsonIgnore
        public boolean isValid() {
            return this.from != null && this.to != null;
        }
    }
}
