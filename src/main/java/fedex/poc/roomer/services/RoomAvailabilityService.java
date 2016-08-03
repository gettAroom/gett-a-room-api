package fedex.poc.roomer.services;

import fedex.poc.roomer.model.Meeting;
import fedex.poc.roomer.model.Room;
import fedex.poc.roomer.repositories.ActionsRepository;
import fedex.poc.roomer.utils.DateUtils;

import java.time.ZonedDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Optional;

/**
 * Created by tg60668 on 27/7/2016.
 */
public class RoomAvailabilityService {
    private static final Integer DEFAULT_STEPS = 15;
    private static final Integer DEFAULT_SMALL_STEPS = 1;
    private ActionsRepository actionsRepository;

    public RoomAvailabilityService(ActionsRepository actionsRepository) {
        this.actionsRepository = actionsRepository;
    }

    public boolean isRoomAvailable(Room room) {
        Optional<ZonedDateTime> lastRoomReservationTime = actionsRepository.getLastRoomReservationTime(room.id);

        if (lastRoomReservationTime.isPresent()) return false;

        Meeting nextMeeting = room.nextMeeting;
        if (isValidMeeting(nextMeeting)) {
            ZonedDateTime nextMeetingStart = DateUtils.toUTC(DateUtils.parse(nextMeeting.startTime));
            ZonedDateTime nextMeetingEnd = DateUtils.toUTC(DateUtils.parse(nextMeeting.endTime));
            ZonedDateTime currentDateTime = DateUtils.toUTC(ZonedDateTime.now());
            return currentDateTime.isAfter(nextMeetingEnd) || currentDateTime.isBefore(nextMeetingStart);
        }
        return true;
    }

    public int getRoomStepsMinutes(Room room) {
        Meeting nextMeeting = room.nextMeeting;
        if (isValidMeeting(nextMeeting)) {
            ZonedDateTime nextMeetingStart = DateUtils.toUTC(DateUtils.parse(nextMeeting.startTime));
            ZonedDateTime currentDateTime = DateUtils.toUTC(ZonedDateTime.now());
            long untilMin = currentDateTime.until(nextMeetingStart, ChronoUnit.MINUTES);
            return untilMin < DEFAULT_STEPS && untilMin > 0 ? DEFAULT_SMALL_STEPS : DEFAULT_STEPS;
        }

        return DEFAULT_STEPS;
    }

    private boolean isValidMeeting(Meeting nextMeeting) {
        return nextMeeting != null && nextMeeting.startTime != null && nextMeeting.endTime != null;
    }
}
