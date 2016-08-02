package fedex.poc.roomer.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.dropwizard.Configuration;
import io.federecio.dropwizard.swagger.SwaggerBundleConfiguration;

/**
 * Created by TG on 12/07/2016.
 */
class ApplicationConfiguration extends Configuration {


    public ApplicationConfiguration() {}

    @JsonProperty("swagger")
    public SwaggerBundleConfiguration swaggerBundleConfiguration;

    @JsonProperty("gettaroom")
    public GettaRoomConfiguration gettaRoomConfiguration;
}
