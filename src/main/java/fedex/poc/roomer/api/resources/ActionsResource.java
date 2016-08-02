package fedex.poc.roomer.api.resources;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by tg60668 on 20/7/2016.
 */
@Path("/actions")
@Api("/actions")
@Produces(MediaType.APPLICATION_JSON)
public interface ActionsResource {

    @POST
    @ApiOperation(value = "reserve room")
    @Path("reserve/{id}")
    Response reserveRoom(@PathParam("id") String roomId,
                     @QueryParam("from") String fromDate,
                     @QueryParam("to") String toDate);
}
