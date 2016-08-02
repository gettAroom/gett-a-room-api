package fedex.poc.roomer.services;

import fedex.poc.roomer.model.Meeting;
import fedex.poc.roomer.model.Room;
import fedex.poc.roomer.repositories.ActionsRepository;
import fedex.poc.roomer.utils.DateUtils;

import java.time.ZonedDateTime;
import java.util.Optional;

/**
 * Created by tg60668 on 27/7/2016.
 */
public class RoomAvailabilityService {
    private ActionsRepository actionsRepository;

    public RoomAvailabilityService(ActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    public boolean isRoomAvailable(Room room) {
        Optional<ZonedDateTime> lastRoomReservationTime = actionsRepository.getLastRoomReservationTime(room.id);

        if (lastRoomReservationTime.isPresent()) return false;

        Meeting nextMeeting = room.nextMeeting;
        if (nextMeeting != null && nextMeeting.startTime != null && nextMeeting.endTime != null) {
            ZonedDateTime nextMeetingStart = DateUtils.toUTC(DateUtils.parse(nextMeeting.startTime));
            ZonedDateTime nextMeetingEnd = DateUtils.toUTC(DateUtils.parse(nextMeeting.endTime));
            ZonedDateTime currentDateTime = DateUtils.toUTC(ZonedDateTime.now());
            return currentDateTime.isAfter(nextMeetingEnd) || currentDateTime.isBefore(nextMeetingStart);
        }
        return true;
    }
}
