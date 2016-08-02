package fedex.poc.roomer.api.resources;

import fedex.poc.roomer.api.model.RoomRep;
import fedex.poc.roomer.conf.RoomsMapper;
import fedex.poc.roomer.model.Room;
import fedex.poc.roomer.repositories.RoomsRepository;
import fedex.poc.roomer.services.RoomAvailabilityService;

import javax.ws.rs.core.Response;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by tg60668 on 20/7/2016.
 */
public class RoomsResourceImpl implements RoomsResource {

    private RoomsRepository roomsRepository;
    private RoomAvailabilityService roomAvailabilityService;

    public RoomsResourceImpl(RoomsRepository roomsRepository, RoomAvailabilityService roomAvailabilityService) {
        this.roomsRepository = roomsRepository;
        this.roomAvailabilityService = roomAvailabilityService;
    }

    @Override
    public Response getRoom(String roomId) {

        Optional<Room> roomOptional = roomsRepository.findById(roomId);

        if (!roomOptional.isPresent()) {
            return Response.noContent().build();
        }

        RoomRep roomRep = RoomRep.fromRoom(roomOptional.get(), roomAvailabilityService);

        if (!roomRep.isAvailable()) {
            List<RoomRep> fallbacks = RoomsMapper.getFallbacks(roomId)
                    .stream()
                    .map(roomsRepository::findById)
                    .filter(Optional::isPresent)
                    .map(r -> RoomRep.fromRoom(r.get(), roomAvailabilityService))
                    .filter(RoomRep::isAvailable)
                    .collect(Collectors.toList());
            roomRep.setFallbacks(fallbacks);
        }

        return Response.ok(roomRep).build();
    }

    @Override
    public Response getRooms() {
        Collection<Room> rooms = roomsRepository.getAll();
        if (rooms.isEmpty()) {
            return Response.noContent().build();
        }
        List<RoomRep> roomReps = rooms.stream()
                .map(r -> RoomRep.fromRoom(r, roomAvailabilityService))
                .collect(Collectors.toList());
        return Response.ok(roomReps).build();
    }
}
