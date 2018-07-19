package xcode.springcloud.dispatchservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

@RestController
public class DispatchServiceController {

    @Autowired
    private LocationServiceFeignClient locationServiceFeignClient;

    @RequestMapping(value = "/drivers/{id}/locations/{locationId}", method = RequestMethod.GET)
    public ResponseEntity<Location> getLocationViaFeign(@PathVariable("id") String id,
                                                        @PathVariable("locationId") String locationId) {

        // Invoke Feign client
        Location location = this.locationServiceFeignClient.getDriverLocation(id, locationId);

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(location, HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getLocationsViaFeign(@PathVariable("id") String id) {

        // Invoke Feign client
        List<Location> locations = this.locationServiceFeignClient.getDriverLocations(id);

        if (locations == null || locations.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        }
    }
}

