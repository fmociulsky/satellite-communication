package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.model.Position;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class PositionService {

    @Autowired
    PositionBuilder positionBuilder;

    public Position getPosition(HashMap<String, Float> distances) throws PositionException{
        positionBuilder.processDistances(distances);
        return positionBuilder.calculatePosition();
    }

    public Position buildMessage() throws PositionException {
        return positionBuilder.calculatePosition();
    }

    public void processDistance(String key, Float distance) throws PositionException {
        final HashMap<String, Float> distances = new HashMap<>();
        distances.put(key, distance);
        positionBuilder.processDistances(distances);
    }

    public void registerSatellite(String satellite_name, Position position) {
        positionBuilder.registerSatellitePosition(satellite_name, position);
    }



    public void unregisterSatellites() {
        positionBuilder.unregisterSatellites();
    }
}
