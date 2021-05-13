package mlchallenge.fuegoquasar.service.message;

import mlchallenge.fuegoquasar.FuegoQuasarApplication;
import mlchallenge.fuegoquasar.controller.SatelliteDataFixture;
import mlchallenge.fuegoquasar.controller.SatelliteRequestData;
import mlchallenge.fuegoquasar.controller.SatelliteRequestDataList;
import mlchallenge.fuegoquasar.service.SatelliteServiceUtil;
import mlchallenge.fuegoquasar.util.JSONUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;


import java.util.HashMap;

import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.MESSAGE_NOT_COMPLETED;
import static mlchallenge.fuegoquasar.service.message.MessageTestData.MESSAGE;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegoQuasarApplication.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class MessageServiceTest {

    @Autowired
    private MessageService messageService;

    public static final JSONUtils.JSONParser<SatelliteRequestDataList> parser = new JSONUtils.JSONParser<>(SatelliteRequestDataList.class);

    @Test
    public void testSingleMessageOK() throws Exception {
        final SatelliteRequestDataList satelliteRequestDataList = parser.parse(SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);

        final String messageProcessed = messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap));
        assertThat(messageProcessed).isNotNull();
        assertThat(messageProcessed).isEqualTo(MESSAGE);
    }

    @Test
    public void testSingleMessageOK2() throws Exception {
        final SatelliteRequestDataList satelliteRequestDataList = parser.parse(SatelliteDataFixture.SATELLITES_COMPLETE_2_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);
        final String messageProcessed = messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap));
        assertThat(messageProcessed).isNotNull();
        assertThat(messageProcessed).isEqualTo(MESSAGE);
    }

    @Test
    public void testBatchedMessageOK() throws Exception {
        final SatelliteRequestDataList satelliteRequestDataList = parser.parse(SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap = getSatelliteMap(satelliteRequestDataList);
        satelliteMap.forEach((key, value)-> {
            messageService.processMessage(key, value.getMessage());
        });

        final String messageProcessed = messageService.buildMessage();
        assertThat(messageProcessed).isNotNull();
        assertThat(messageProcessed).isEqualTo(MESSAGE);
    }

    @Test
    public void testBuildMessageMultipleBatchesAndTries() throws Exception {
        //limpiar el service
        final SatelliteRequestDataList satelliteRequestDataList1 = parser.parse(SatelliteDataFixture.SATELLITES_SINGLE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap1 = getSatelliteMap(satelliteRequestDataList1);
        assertThatExceptionOfType(MessageException.class).isThrownBy(() ->
                messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap1)))
                .withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());

        final SatelliteRequestDataList satelliteRequestDataList2 = parser.parse(SatelliteDataFixture.SATELLITES_SINGLE_2_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap2 = getSatelliteMap(satelliteRequestDataList2);
        assertThatExceptionOfType(MessageException.class).isThrownBy(() ->
                messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap2)))
                .withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());

        final SatelliteRequestDataList satelliteRequestDataList3 = parser.parse(SatelliteDataFixture.SATELLITES_SINGLE_3_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap3 = getSatelliteMap(satelliteRequestDataList3);

        final String messageProcessed = messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap3));
        assertThat(messageProcessed).isNotNull();
        assertThat(messageProcessed).isEqualTo(MESSAGE);
    }

    @Test
    public void testMessageIncompleteException() throws Exception {
        final SatelliteRequestDataList satelliteRequestDataList = parser.parse(SatelliteDataFixture.SATELLITES_INCOMPLETE_1_JSON);
        final HashMap<String, SatelliteRequestData> satelliteMap1 = getSatelliteMap(satelliteRequestDataList);
        assertThatExceptionOfType(MessageException.class).isThrownBy(() -> messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satelliteMap1))).withMessageContaining(MESSAGE_NOT_COMPLETED.getValue());
    }

    private HashMap<String, SatelliteRequestData> getSatelliteMap(SatelliteRequestDataList satelliteRequestDataList){
        final HashMap<String, SatelliteRequestData> satelliteMap = new HashMap<>();
        satelliteRequestDataList.getSatellites().forEach(satelliteRequestData ->
                satelliteMap.put(satelliteRequestData.getName(), satelliteRequestData));
        return satelliteMap;
    }


}
