package xcode.springcloud.dispatchservice;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.apache.commons.lang.NotImplementedException;
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

    @HystrixCommand(fallbackMethod = "getDefaultLocationsViaFeign")
    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getLocationsViaFeign(
            @PathVariable("id") String id) {

        // Invoke Feign client
        List<Location> locations = this.locationServiceFeignClient.getDriverLocations(id);

        if (locations == null || locations.isEmpty()) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(locations, HttpStatus.OK);
        }
    }

    public ResponseEntity<List<Location>> getDefaultLocationsViaFeign(
            String id) {
        List<Location> locations = new ArrayList<>();
        return new ResponseEntity<>(locations, HttpStatus.OK);
    }

    @RequestMapping(value = "/Rides", method = RequestMethod.POST)
    public void requestRide(){
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/Rides/{id}", method = RequestMethod.GET)
    public void getRide(
            @PathVariable("id") String id){
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/Rides/{id}/check", method = RequestMethod.POST)
    public void checkRide(){
        throw new NotImplementedException();
    }

    @RequestMapping(value = "/Rides/{id}", method = RequestMethod.PUT)
    public void updateRide(
            @PathVariable("id") String id){
        throw new NotImplementedException();
    }
}

