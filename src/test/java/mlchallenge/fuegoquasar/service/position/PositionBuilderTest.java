package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.FuegoQuasarApplication;
import mlchallenge.fuegoquasar.model.Position;
import org.junit.Test;

import static mlchallenge.fuegoquasar.service.position.PositionException.PositionExceptionError.NO_SATELLITE_POSITION;
import static mlchallenge.fuegoquasar.service.position.PositionTestData.POSITION_1;
import static mlchallenge.fuegoquasar.service.position.PositionTestData.POSITION_2;
import static mlchallenge.fuegoquasar.service.position.PositionTestData.POSITION_3;
import static org.assertj.core.api.Assertions.assertThat;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegoQuasarApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PositionBuilderTest {

    @Autowired
    PositionBuilder positionBuilder;

    @Test
    public void registerSatellitePositionTest(){
        positionBuilder.registerSatellitePosition("kenobi", POSITION_1);
        assertThat(positionBuilder.getSatellitePositions().size()).isEqualTo(1);
    }

    @Test
    public void unregisterSatellitePositionTest(){
        registerSatellitePositionTest();
        positionBuilder.unregisterSatellites();
        assertThat(positionBuilder.getSatellitePositions().size()).isEqualTo(0);
    }

    @Test
    public void testProcessPositionOK() throws PositionException {
        positionBuilder.registerSatellitePosition("kenobi", POSITION_1);
        positionBuilder.registerSatellitePosition("sato", POSITION_2);
        positionBuilder.registerSatellitePosition("skywalker", POSITION_3);
        final HashMap<String, Float> distancesMap = new HashMap<String, Float>();
        distancesMap.put("kenobi", 670.82f);
        distancesMap.put("sato", 400f);
        distancesMap.put("skywalker", 200f);
        positionBuilder.processDistances(distancesMap);
        assertThat(positionBuilder.getSatellitePositions().values().stream().filter(satellitePositionData -> satellitePositionData.getDistance() == null)).isEmpty();
    }

    @Test
    public void testSatellitePositionError() {
        positionBuilder.registerSatellitePosition("kenobi", POSITION_1);
        positionBuilder.registerSatellitePosition("sato", POSITION_2);
        final HashMap<String, Float> distancesMap = new HashMap<String, Float>();
        distancesMap.put("kenobi", 670.82f);
        distancesMap.put("sato", 400f);
        distancesMap.put("skywalker", 200f);
        try {
            positionBuilder.processDistances(distancesMap);
        } catch (final Exception exception ) {
            assertThat(exception instanceof PositionException).isTrue();
            assertThat(exception.getMessage()).isEqualTo(String.format(NO_SATELLITE_POSITION.getValue(),"skywalker"));
        }
    }

    @Test
    public void testBuildPositionOK(){
        testProcessPositionOK();
        Position position = positionBuilder.calculatePosition();
    }


}
