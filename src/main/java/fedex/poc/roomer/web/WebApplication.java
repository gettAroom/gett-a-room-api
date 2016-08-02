package fedex.poc.roomer.web;

import fedex.poc.roomer.api.resources.ActionsResource;
import fedex.poc.roomer.api.resources.ActionsResourceImpl;
import fedex.poc.roomer.api.resources.RoomsResource;
import fedex.poc.roomer.api.resources.RoomsResourceImpl;
import fedex.poc.roomer.cache.CacheManager;
import fedex.poc.roomer.model.Action;
import fedex.poc.roomer.model.RoomsSnapshot;
import fedex.poc.roomer.mq.dao.DataReader;
import fedex.poc.roomer.mq.dao.MQDataAccess;
import fedex.poc.roomer.mq.manager.MQManager;
import fedex.poc.roomer.repositories.*;
import fedex.poc.roomer.services.RoomAvailabilityService;
import io.dropwizard.Application;
import io.dropwizard.assets.AssetsBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import io.dropwizard.util.Duration;
import io.federecio.dropwizard.swagger.SwaggerBundle;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;
import org.eclipse.jetty.servlets.CrossOriginFilter;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import java.util.EnumSet;
import java.util.concurrent.ExecutorService;

/**
 * Created by TG on 12/07/2016.
 */
public class WebApplication extends Application<ApplicationConfiguration> {

    public static void main(String[] args) throws Exception {
        new WebApplication().run(args);
    }

    @Override
    public void initialize(Bootstrap<ApplicationConfiguration> bootstrap) {

        bootstrap.addBundle(new AssetsBundle("/static", "/", "index.html"));
        bootstrap.addBundle(new AssetsBundle("/static/styles", "/styles", null, "styles"));
        bootstrap.addBundle(new AssetsBundle("/static/js", "/js", null, "js"));
        bootstrap.addBundle(new AssetsBundle("/static/images", "/images", null, "images"));

        bootstrap.addBundle(new SwaggerBundle<ApplicationConfiguration>() {
            @Override
            protected SwaggerBundleConfiguration getSwaggerBundleConfiguration(ApplicationConfiguration applicationConfiguration) {
                return applicationConfiguration.swaggerBundleConfiguration;
            }
        });
    }

    public void run(ApplicationConfiguration conf, Environment environment) throws Exception {
        // Enable CORS headers
        final FilterRegistration.Dynamic cors =
                environment.servlets().addFilter("CORS", CrossOriginFilter.class);

        // Configure CORS parameters
        cors.setInitParameter("allowedOrigins", "*");
        cors.setInitParameter("allowedHeaders", "X-Requested-With,Content-Type,Accept,Origin");
        cors.setInitParameter("allowedMethods", "OPTIONS,GET,PUT,POST,DELETE,HEAD");

        // Add URL mapping
        cors.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");


        // ExecutorServices
        ExecutorService asyncRoomsSnapshotRead = environment.lifecycle()
                .executorService("asyncRead")
                .maxThreads(1).build();
        ExecutorService asyncWriting = environment.lifecycle()
                .executorService("asyncWriting")
                .minThreads(1)
                .maxThreads(5)
                .keepAliveTime(Duration.minutes(1)).build();

        // DI
        MQManager mqManager = new MQManager(conf.gettaRoomConfiguration.useProxy);
        DataReader<RoomsSnapshot> roomsSnapshotDataReader =
                new MQDataAccess<>(mqManager, conf.gettaRoomConfiguration.roomStatusQueueURL, RoomsSnapshot.class);
        CacheManager<RoomsSnapshot> roomsSnapshotCacheManager = new CacheManager<>(roomsSnapshotDataReader);
        RoomsSnapshotDAO roomsSnapshotDao = new RoomsSnapshotDAO(roomsSnapshotCacheManager);
        RoomsRepository roomsRepository = new RoomsRepositoryImpl(roomsSnapshotDao);

        MQDataAccess<Action> actionMQDataAccess =
                new MQDataAccess<>(mqManager, conf.gettaRoomConfiguration.actionsQueueURL, Action.class);
        ActionsDAO actionsDAO = new ActionsDAO(actionMQDataAccess);
        ActionsRepository actionsRepository = new ActionsRepositoryImpl(actionsDAO, asyncWriting);

        asyncRoomsSnapshotRead.execute(roomsSnapshotCacheManager);

        RoomAvailabilityService roomAvailabilityService = new RoomAvailabilityService(actionsRepository);
        RoomsResource roomsResource = new RoomsResourceImpl(roomsRepository, roomAvailabilityService);
        ActionsResource actionsResource = new ActionsResourceImpl(actionsRepository, roomsResource);
        // Register resources
        environment.jersey().register(roomsResource);
        environment.jersey().register(actionsResource);

    }
}
