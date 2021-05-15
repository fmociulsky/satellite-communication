package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.model.Position;
import org.springframework.stereotype.Component;

import java.util.HashMap;

import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.PositionCalculatorExceptionError.INVALID_DISTANCE;
import static mlchallenge.fuegoquasar.service.position.PositionException.PositionExceptionError.NO_SATELLITE_POSITION;

@Component
public class PositionBuilder {

    private HashMap<String, SatellitePositionData> satellitePositions;

    public PositionBuilder() {
        satellitePositions = new HashMap<>();
    }

    public HashMap<String, SatellitePositionData> getSatellitePositions() {
        return satellitePositions;
    }

    public void processDistances(HashMap<String, Float> distances) throws PositionException{
        for (final String key : distances.keySet()) {
            final SatellitePositionData satellitePositionData = satellitePositions.get(key);
            if(distances.get(key) < 0) throw new PositionException(String.format(INVALID_DISTANCE.getValue(), key));
            if (satellitePositionData == null) throw new PositionException(String.format(NO_SATELLITE_POSITION.getValue(), key));
            satellitePositionData.setDistance(distances.get(key));
            satellitePositions.replace(key, satellitePositionData);
        }
    }

    public Position calculatePosition() throws PositionException {
        try {
            return PositionCalculator.calculatePosition(satellitePositions);
        } catch (final PositionCalculatorException exceptionMsg) {
            throw new PositionException(exceptionMsg.getMessage());
        }
    }

    public void registerSatellitePosition(String satellite_name, Position position){
        satellitePositions.put(satellite_name, new SatellitePositionData(position.getX(), position.getY()));
    }

    public void unregisterSatellites() {
        satellitePositions = new HashMap<>();
    }
}
