package mlchallenge.fuegoquasar.service;

import mlchallenge.fuegoquasar.controller.HansoloControllerResponse;
import mlchallenge.fuegoquasar.model.Position;
import mlchallenge.fuegoquasar.controller.SatelliteRequestData;
import mlchallenge.fuegoquasar.controller.SatelliteRequestDataList;
import mlchallenge.fuegoquasar.service.message.MessageException;
import mlchallenge.fuegoquasar.service.message.MessageService;
import mlchallenge.fuegoquasar.service.position.PositionException;
import mlchallenge.fuegoquasar.service.position.PositionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

@Service
public class SatelliteService {

    HashMap<String, SatelliteRequestData> satellitesData;

    @Autowired
    private MessageService messageService;

    @Autowired
    private PositionService positionService;

    final List<String> messages = new ArrayList<>();

    public HansoloControllerResponse getMessageAndPosition(SatelliteRequestDataList satellitesRequestDataList) throws HanSoloException {
        satellitesRequestDataList.getSatellites().forEach(this::updateSatellitesData);
        return buildDataResponse();
    }

    public HansoloControllerResponse getMessageAndPositionSplited(SatelliteRequestData satelliteRequestData) throws HanSoloException {
        updateSatellitesData(satelliteRequestData);
        return buildDataResponse();
    }

    private HansoloControllerResponse buildDataResponse() throws HanSoloException{
        try{
            final Position position = positionService.getPosition(SatelliteServiceUtil.getSatellitesDistances(satellitesData));
            final String mensaje = messageService.getMessage(SatelliteServiceUtil.getSatellitesMessages(satellitesData));
            return new HansoloControllerResponse(position, mensaje);
        }catch (final MessageException | PositionException msgException){
            throw new HanSoloException(msgException.getMessage());
        }
    }

    private void updateSatellitesData(SatelliteRequestData satelliteRequestData) {
        if(satellitesData == null) satellitesData = new HashMap<>();
        satellitesData.put(satelliteRequestData.getName(), satelliteRequestData);
    }

    public void registerSatellite(String satellite_name, Position position) {
        positionService.registerSatellite(satellite_name, position);
    }

    public void unregisterSatellites() {
        positionService.unregisterSatellites();
    }
}
