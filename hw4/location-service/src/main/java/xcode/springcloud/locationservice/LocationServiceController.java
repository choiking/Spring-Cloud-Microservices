package xcode.springcloud.locationservice;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;

import com.netflix.appinfo.InstanceInfo;
import com.netflix.discovery.DiscoveryClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class LocationServiceController {

    private final AtomicLong counter = new AtomicLong();
    private Random random = new Random();

    // Key: driver Id
    // Value: a list of driver locations
    private HashMap<String, DriverLocations> locationsMap = new HashMap<>();

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.POST)
    public ResponseEntity<Location> create(
            @PathVariable("id") String id,
            @RequestBody(required = false) Location inputLocation) {
        Location location;
        if (inputLocation == null) {
            location = new Location(
                    random.nextInt(90),
                    random.nextInt(90));
        } else {
            location = new Location(
                    inputLocation.getLatitude(),
                    inputLocation.getLongitude());
        }

        if (!locationsMap.containsKey(id)) {
            locationsMap.put(id, new DriverLocations(id));
        }

        locationsMap.get(id).addLocation(location);
        return new ResponseEntity<>(location, HttpStatus.CREATED);
    }

    @RequestMapping(value = "/drivers/{id}/locations", method = RequestMethod.GET)
    public ResponseEntity<List<Location>> getAll(
            @PathVariable("id") String id
    ) {
        if (!DriverController.isDriverValid(id)) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.BAD_REQUEST);
        }

        DriverLocations driverLocations = locationsMap.get(id);

        if (driverLocations == null) {
            return new ResponseEntity<>(new ArrayList<>(), HttpStatus.OK);
        }

        return new ResponseEntity<>(driverLocations.getAll(), HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}/locations/{locationId}", method = RequestMethod.GET)
    public ResponseEntity<Location> get(@PathVariable("id") String id,
                                        @PathVariable("locationId") String locationId) {
        Location location = null;
        if (!locationsMap.containsKey(id)) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }
        DriverLocations driverLocations = locationsMap.get(id);

        location = driverLocations.getLocation(Long.parseLong(locationId));

        if (location == null) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        } else {
            return new ResponseEntity<>(driverLocations.getLastLocation(), HttpStatus.OK);
        }
    }

    @RequestMapping(value = "/drivers/{id}/locations/current", method = RequestMethod.GET)
    public ResponseEntity<Location> getCurrentLocation(@PathVariable("id") String id) {
        Location location = null;
        if (!locationsMap.containsKey(id)) {
            return new ResponseEntity<>(location, HttpStatus.BAD_REQUEST);
        }
        DriverLocations driverLocations = locationsMap.get(id);

        return new ResponseEntity<>(driverLocations.getLastLocation(), HttpStatus.OK);
    }

    @RequestMapping(value = "/drivers/{id}/locations/{locationId}", method = RequestMethod.PUT)
    public ResponseEntity<Location> update(
            @RequestBody Location location,
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        Location temp = null;
        if (!locationsMap.containsKey(id)) {
            return new ResponseEntity<>(temp, HttpStatus.BAD_REQUEST);
        }
        DriverLocations driverLocations = locationsMap.get(id);

        if (driverLocations.updateLocation(Long.parseLong(locationId), location)) {
            return new ResponseEntity<>(
                    driverLocations.getLocation(Long.parseLong(locationId)),
                    HttpStatus.OK);
        } else {
            return new ResponseEntity<>(temp, HttpStatus.BAD_REQUEST);
        }
    }

    @RequestMapping(value = "/drivers/{id}/locations/{locationId}", method = RequestMethod.DELETE)
    public ResponseEntity<Location> delete(
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        return this.deleteImpl(id, locationId);
    }

    @RequestMapping(value = "/drivers/{id}/locations/{locationId}/delete", method = RequestMethod.POST)
    public ResponseEntity<Location> deleteByPost(
            @PathVariable("id") String id,
            @PathVariable("locationId") String locationId) {
        return this.deleteImpl(id, locationId);
    }

    private ResponseEntity<Location> deleteImpl(String id, String locationId) {
        Location temp = null;
        if (!locationsMap.containsKey(id)) {
            return new ResponseEntity<>(temp, HttpStatus.BAD_REQUEST);
        }
        DriverLocations driverLocations = locationsMap.get(id);

        if (driverLocations.deleteLocation(Long.parseLong(locationId))) {
            return new ResponseEntity<>(temp, HttpStatus.NO_CONTENT);
        } else {
            return new ResponseEntity<>(temp, HttpStatus.BAD_REQUEST);
        }
    }
}
