package fedex.poc.roomer.api.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tg60668 on 20/7/2016.
 */
@Path("/rooms")
@Api("/rooms")
@Produces(MediaType.APPLICATION_JSON)
@Consumes(MediaType.APPLICATION_JSON)
public interface RoomsResource {

    @GET
    @ApiOperation(value = "Get room info by id")
    @Path("/{id}")
    Response getRoom(@PathParam("id") String roomId);

    @GET
    @ApiOperation(value = "Gett all rooms info")
    @Path("/")
    Response getRooms();

}
