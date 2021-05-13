package mlchallenge.fuegoquasar.service.position;

import mlchallenge.fuegoquasar.FuegoQuasarApplication;
import mlchallenge.fuegoquasar.controller.SatelliteDataFixture;
import mlchallenge.fuegoquasar.controller.SatelliteRequestData;
import mlchallenge.fuegoquasar.controller.SatelliteRequestDataList;
import mlchallenge.fuegoquasar.model.Position;
import mlchallenge.fuegoquasar.service.SatelliteServiceUtil;
import mlchallenge.fuegoquasar.util.JSONUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.HashMap;

import static mlchallenge.fuegoquasar.service.position.PositionException.PositionExceptionError.NO_SATELLITE_POSITION;
import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.TrilateralizationExceptionError.INVALID_DISTANCE;
import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.TrilateralizationExceptionError.SATELLITES_INFO_MISMATCH;
import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.TrilateralizationExceptionError.SATELLITE_QTY_NOT_ENOUGH;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegoQuasarApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class PositionServiceTest {

    @Autowired
    private PositionService positionService;

    public static final JSONUtils.JSONParser<SatelliteRequestDataList> parserSatelite = new JSONUtils.JSONParser<>(SatelliteRequestDataList.class);
    public static final JSONUtils.JSONParser<Position> parserPosition = new JSONUtils.JSONParser<>(Position.class);

    @Test
    public void testPositionOK() throws Exception {
        register3Satellites();
        final SatelliteRequestDataList satelliteRequestDataList = parserSatelite.parse(SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);

        final Position position = positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satelliteMap));
        assertThat(position).isNotNull();
    }

    @Test
    public void testPositionNoSatelliteRegisteredError() throws Exception {
        final SatelliteRequestDataList satelliteRequestDataList = parserSatelite.parse(SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);
        final String firstSatellite = satelliteMap.keySet().stream().findFirst().get();

        assertThatExceptionOfType(PositionException.class).isThrownBy(() ->
                positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satelliteMap)))
                .withMessageContaining(String.format(NO_SATELLITE_POSITION.getValue(), firstSatellite));
    }

    @Test
    public void testPositionNotEnoughSatelliteRegisteredError() throws Exception {
        register2Satellites();
        final SatelliteRequestDataList satelliteRequestDataList = parserSatelite.parse(SatelliteDataFixture.SATELLITES_INCOMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);

        assertThatExceptionOfType(PositionException.class).isThrownBy(() ->
                positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satelliteMap)))
                .withMessageContaining(SATELLITE_QTY_NOT_ENOUGH.getValue());
    }

    @Test
    public void testPositionWrongInfoError() throws Exception {
        register3SatellitesLong();
        final SatelliteRequestDataList satelliteRequestDataList = parserSatelite.parse(SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);

        assertThatExceptionOfType(PositionException.class).isThrownBy(() ->
                positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satelliteMap)))
                .withMessageContaining(SATELLITES_INFO_MISMATCH.getValue());
    }

    @Test
    public void testPositionInvalidDistanceError() throws Exception {
        register3SatellitesLong();
        final SatelliteRequestDataList satelliteRequestDataList = parserSatelite.parse(SatelliteDataFixture.SATELLITES_WRONG_DISTANCE_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);

        assertThatExceptionOfType(PositionException.class).isThrownBy(() ->
                positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satelliteMap)))
                .withMessageContaining(String.format(INVALID_DISTANCE.getValue(), "kenobi"));
    }

    @Test
    public void testRegisterSatellite() throws Exception{
        final Position position = parserPosition.parse(SatelliteDataFixture.POSITION_1);
        positionService.registerSatellite("kenobi", position);
        assertThat(positionService.positionBuilder.getSatellitePositions().size()).isEqualTo(1);
        final SatellitePositionData kenobiPosition = positionService.positionBuilder.getSatellitePositions().get("kenobi");
        assertThat(kenobiPosition).isNotNull();
        assertThat(kenobiPosition).isEqualTo(new SatellitePositionData(-500,-200));
    }

    @Test
    public void testUnregisterSatellites() throws Exception{
        testRegisterSatellite();
        positionService.unregisterSatellites();
        assertThat(positionService.positionBuilder.getSatellitePositions().size()).isEqualTo(0);
    }


    private void register2Satellites() {
        positionService.registerSatellite("kenobi", new Position(-500, -200));
        positionService.registerSatellite("sato", new Position(500, 100));
    }

    private void register3SatellitesLong() {
        positionService.registerSatellite("kenobi", new Position(-5000, -2000));
        positionService.registerSatellite("skywalker", new Position(100, -100));
        positionService.registerSatellite("sato", new Position(500, 100));
    }

    private void register3Satellites() {
        positionService.registerSatellite("kenobi", new Position(-500, -200));
        positionService.registerSatellite("skywalker", new Position(100, -100));
        positionService.registerSatellite("sato", new Position(500, 100));
    }

    private HashMap<String, SatelliteRequestData> getSatelliteMap(SatelliteRequestDataList satelliteRequestDataList){
        final HashMap<String, SatelliteRequestData> satelliteMap = new HashMap<>();
        satelliteRequestDataList.getSatellites().forEach(satelliteRequestData ->
                satelliteMap.put(satelliteRequestData.getName(), satelliteRequestData));
        return satelliteMap;
    }
}
