package fedex.poc.roomer.api.resources;

import fedex.poc.roomer.api.model.RoomRep;
import fedex.poc.roomer.repositories.ActionsRepository;
import fedex.poc.roomer.utils.DateUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.BadRequestException;
import javax.ws.rs.core.Response;
import java.util.Optional;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class ActionsResourceImpl implements ActionsResource {

    private static final Logger LOGGER = Logger.getLogger(ActionsResourceImpl.class);

    private ActionsRepository actionsRepository;
    private RoomsResource roomsResource;

    public ActionsResourceImpl(ActionsRepository actionsRepository, RoomsResource roomsResource) {
        this.actionsRepository = actionsRepository;
        this.roomsResource = roomsResource;
    }

    @Override
    public Response reserveRoom(String roomId,
                         String fromDate,
                         String toDate) {

        LOGGER.info(String.format("Get a request for room reservation id=%s,from=%s,to=%s", roomId, fromDate, toDate));

        if (!DateUtils.isValidZonedDateTime(fromDate) || !DateUtils.isValidZonedDateTime(toDate)) {
            throw new BadRequestException();
        }

        Optional<RoomRep> requestedRoom = Optional.ofNullable((RoomRep) roomsResource.getRoom(roomId).getEntity())
                .filter(RoomRep::isAvailable);
        if (!requestedRoom.isPresent()) return Response.notAcceptable(null).build();

        actionsRepository.saveAsyncRoomReservation(roomId, fromDate, toDate);

        return Response.accepted().build();
    }
}
