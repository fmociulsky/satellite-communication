package mlchallenge.fuegoquasar.controller;

import mlchallenge.fuegoquasar.model.Position;
import mlchallenge.fuegoquasar.service.HanSoloException;
import mlchallenge.fuegoquasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;

@RestController

public class HanSoloController {

    @Autowired
    private SatelliteService satelliteService;

    @PostMapping(value = "/topsecret/satellite/{satellite_name}")
    public ResponseEntity<String> registerSatellite(@RequestBody Position position, @PathVariable String satellite_name) {
        try {
            satelliteService.registerSatellite(satellite_name, position);
            return new ResponseEntity<>(String.format("Satellite %s registered", satellite_name), HttpStatus.OK);
        } catch (final Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), NOT_FOUND);
        }
    }

    @DeleteMapping(value = "/topsecret/satellite")
    public ResponseEntity<String> removeResisteredSatellites() {
        try {
            satelliteService.unregisterSatellites();
            return new ResponseEntity<String>("All Satellites unregistered", HttpStatus.OK);
        } catch (final Exception exception) {
            return new ResponseEntity<String>(exception.getMessage(), NOT_FOUND);
        }
    }

    @PostMapping("/topsecret")
    public ResponseEntity<?> getMessageAndPositionFromSatellites(@RequestBody SatelliteRequestDataList satellitesDataList){
        try {
            final HansoloControllerResponse hansoloControllerResponse = satelliteService.getMessageAndPosition(satellitesDataList);
            return new ResponseEntity<HansoloControllerResponse>(hansoloControllerResponse, OK);
        } catch (final HanSoloException exception) {
            return new ResponseEntity<String>(exception.getMessage(), NOT_FOUND);
        }
    }

    @PostMapping("/topsecret_split/{satellite_name}")
    public ResponseEntity<?> getMessageFromSatellites(@PathVariable(value = "satellite_name") String satellite_name,
                                                              @RequestBody SatelliteRequestData satelliteRequestData){
        satelliteRequestData.setName(satellite_name);
        try {
            final HansoloControllerResponse hansoloControllerResponse = satelliteService.getMessageAndPositionSplited(satelliteRequestData);
            return new ResponseEntity<HansoloControllerResponse>(hansoloControllerResponse, OK);
        } catch (final HanSoloException exception) {
            return new ResponseEntity<String>(exception.getMessage(), NOT_FOUND);
        }
    }
}
