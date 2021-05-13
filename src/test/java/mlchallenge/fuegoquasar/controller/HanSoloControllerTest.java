package mlchallenge.fuegoquasar.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import mlchallenge.fuegoquasar.FuegoQuasarApplication;
import mlchallenge.fuegoquasar.model.Position;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.file.Files;
import java.nio.file.Paths;

import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.OK_MESSAGE_1;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.POSITION_1;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.POSITION_2;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.POSITION_3;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.SATELLITES_COMPLETE_1_JSON;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.SATELLITES_COMPLETE_1_MSG_INCOMPLETE_JSON;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.SATELLITES_INCOMPLETE_1_JSON;
import static mlchallenge.fuegoquasar.controller.SatelliteDataFixture.SATELLITE_3_JSON;
import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.MESSAGE_NOT_COMPLETED;
import static mlchallenge.fuegoquasar.service.position.PositionCalculatorException.TrilateralizationExceptionError.SATELLITE_QTY_NOT_ENOUGH;
import static mlchallenge.fuegoquasar.service.position.PositionException.PositionExceptionError.NO_SATELLITE_POSITION;
import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.http.HttpStatus.NOT_FOUND;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = FuegoQuasarApplication.class)
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class HanSoloControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    public void testRegisterSatellites() throws Exception {
        final String position1 = new String(Files.readAllBytes(Paths.get(POSITION_1)));
        final MvcResult result1 = mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "kenobi").contentType(MediaType.APPLICATION_JSON).content(position1)).andReturn();
        assertThat(result1.getResponse().getContentAsString()).isEqualTo("Satellite kenobi registered");
        final String position2 = new String(Files.readAllBytes(Paths.get(POSITION_2)));
        final MvcResult result2 = mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "sato").contentType(MediaType.APPLICATION_JSON).content(position2)).andReturn();
        assertThat(result2.getResponse().getContentAsString()).isEqualTo("Satellite sato registered");
        final String position3 = new String(Files.readAllBytes(Paths.get(POSITION_3)));
        final MvcResult result3 = mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "skywalker").contentType(MediaType.APPLICATION_JSON).content(position3)).andReturn();
        assertThat(result3.getResponse().getContentAsString()).isEqualTo("Satellite skywalker registered");
    }

    @Test
    public void testGetMessageIncompleteError() throws Exception {
        testRegisterSatellites();
        final String s = new String(Files.readAllBytes(Paths.get(SATELLITES_COMPLETE_1_MSG_INCOMPLETE_JSON)));
        final MvcResult result = mockMvc.perform(post("/topsecret").contentType(MediaType.APPLICATION_JSON).content(s)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(MESSAGE_NOT_COMPLETED.getValue());
    }


    @Test
    public void testGetMessagePositionOK() throws Exception {
        testRegisterSatellites();
        final String s = new String(Files.readAllBytes(Paths.get(SATELLITES_COMPLETE_1_JSON)));
        final MvcResult result = mockMvc.perform(post("/topsecret").contentType(MediaType.APPLICATION_JSON).content(s)).andReturn();
        final String stringResponse = result.getResponse().getContentAsString();
        final HansoloControllerResponse actualResult = objectMapper.readValue(stringResponse, HansoloControllerResponse.class);
        final HansoloControllerResponse expectedResult = new HansoloControllerResponse(new Position(99.08371f, 99.99774f), OK_MESSAGE_1);

        assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
        assertResult(actualResult, expectedResult);
    }

    @Test
    public void testGetMessagePositionSplitedOK() throws Exception {
        testGetMessageIncompleteError();
        final String s = new String(Files.readAllBytes(Paths.get(SATELLITE_3_JSON)));
        final MvcResult result = mockMvc.perform(post("/topsecret_split/{satellite_name}" , "skywalker").contentType(MediaType.APPLICATION_JSON).content(s)).andReturn();
        final String stringResponse = result.getResponse().getContentAsString();
        final HansoloControllerResponse actualResult = objectMapper.readValue(stringResponse, HansoloControllerResponse.class);
        final HansoloControllerResponse expectedResult = new HansoloControllerResponse(new Position(99.08371f, 99.99774f), OK_MESSAGE_1);
        assertThat(result.getResponse().getStatus()).isEqualTo(OK.value());
        assertResult(actualResult, expectedResult);
    }

    @Test
    public void testGetSatelliteNotRegisteredError() throws Exception {
        final String position1 = new String(Files.readAllBytes(Paths.get(POSITION_1)));
        mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "kenobi").contentType(MediaType.APPLICATION_JSON).content(position1)).andReturn();
        final String position2 = new String(Files.readAllBytes(Paths.get(POSITION_2)));
        mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "sato").contentType(MediaType.APPLICATION_JSON).content(position2)).andReturn();
        final String s = new String(Files.readAllBytes(Paths.get(SATELLITES_COMPLETE_1_JSON)));
        final MvcResult result = mockMvc.perform(post("/topsecret").contentType(MediaType.APPLICATION_JSON).content(s)).andReturn();
        assertThat(result.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(result.getResponse().getContentAsString()).isEqualTo(String.format(NO_SATELLITE_POSITION.getValue(), "skywalker"));
    }

    @Test
    public void testFullOK() throws Exception{
        final String position1 = new String(Files.readAllBytes(Paths.get(POSITION_1)));
        mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "kenobi").contentType(MediaType.APPLICATION_JSON).content(position1)).andReturn();
        final String position2 = new String(Files.readAllBytes(Paths.get(POSITION_2)));
        mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "sato").contentType(MediaType.APPLICATION_JSON).content(position2)).andReturn();
        final String s = new String(Files.readAllBytes(Paths.get(SATELLITES_INCOMPLETE_1_JSON)));
        final MvcResult result1 = mockMvc.perform(post("/topsecret").contentType(MediaType.APPLICATION_JSON).content(s)).andReturn();
        assertThat(result1.getResponse().getStatus()).isEqualTo(NOT_FOUND.value());
        assertThat(result1.getResponse().getContentAsString()).isEqualTo(String.format(SATELLITE_QTY_NOT_ENOUGH.getValue(), "skywalker"));
        final String position3 = new String(Files.readAllBytes(Paths.get(POSITION_3)));
        mockMvc.perform(post("/topsecret/satellite/{satellite_name}" , "skywalker").contentType(MediaType.APPLICATION_JSON).content(position3)).andReturn();
        final String s2 = new String(Files.readAllBytes(Paths.get(SATELLITES_COMPLETE_1_JSON)));
        final MvcResult result2 = mockMvc.perform(post("/topsecret").contentType(MediaType.APPLICATION_JSON).content(s2)).andReturn();
        final String stringResponse = result2.getResponse().getContentAsString();
        final HansoloControllerResponse actualResult = objectMapper.readValue(stringResponse, HansoloControllerResponse.class);
        final HansoloControllerResponse expectedResult = new HansoloControllerResponse(new Position(99.08371f, 99.99774f), OK_MESSAGE_1);
        assertThat(result2.getResponse().getStatus()).isEqualTo(OK.value());
        assertResult(actualResult, expectedResult);

    }

    private void assertResult(HansoloControllerResponse actualResult, HansoloControllerResponse expectedResult) {
        assertThat(actualResult.getMessage()).isEqualTo(expectedResult.getMessage());
        assertThat(actualResult.getPosition().getX()).isEqualTo(expectedResult.getPosition().getX());
        assertThat(actualResult.getPosition().getY()).isEqualTo(expectedResult.getPosition().getY());
    }
}
