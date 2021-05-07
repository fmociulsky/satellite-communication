package mlchallenge.fuegoquasar.controller;

import com.sun.istack.internal.NotNull;
import mlchallenge.fuegoquasar.model.Satelite;
import mlchallenge.fuegoquasar.model.Satellites;
import mlchallenge.fuegoquasar.service.SatelliteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/topsecret")
public class HanSoloController {

    private SatelliteService satelliteService;

    @GetMapping("/message")
    public ResponseEntity<String> getMessage(){
        return new ResponseEntity<>("hola", HttpStatus.OK);
    }

    @GetMapping("/location/{distance}")
    public ResponseEntity<String> getLocation(@PathVariable(value="distance") Long distance){
        return new ResponseEntity<>("hola", HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<HansoloControlleResponse> getMessageFromSatellites(@RequestBody @NotNull Satellites satellites){
        satelliteService = new SatelliteService();
        final String mensaje = satelliteService.getMessage(satellites);
        final HansoloControlleResponse hansoloControlleResponse = new HansoloControlleResponse(null, mensaje);

        return new ResponseEntity<HansoloControlleResponse>(hansoloControlleResponse, HttpStatus.OK);
    }
}
