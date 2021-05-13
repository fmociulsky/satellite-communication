package mlchallenge.fuegoquasar.service;

import mlchallenge.fuegoquasar.controller.SatelliteRequestData;

import java.util.HashMap;
import java.util.List;

public class SatelliteServiceUtil {

    public static HashMap<String, List<String>> getSatellitesMessages(HashMap<String, SatelliteRequestData> satellitesData){
        final HashMap<String, List<String>> messageMap = new HashMap<>();
        satellitesData.forEach((key, value)-> messageMap.put(key, value.getMessage()));
        return messageMap;
    }

    public static HashMap<String, Float> getSatellitesDistances(HashMap<String, SatelliteRequestData> satellitesData){
        final HashMap<String, Float> distanceMap = new HashMap<>();
        satellitesData.forEach((key, value)-> distanceMap.put(key, value.getDistance()));
        return distanceMap;
    }
}
