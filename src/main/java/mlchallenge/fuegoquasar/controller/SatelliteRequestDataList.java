package mlchallenge.fuegoquasar.controller;

import org.springframework.beans.factory.annotation.Autowired;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class SatelliteRequestDataList implements Serializable {

    private static final long serialVersionUID = -304471322353847115L;

    @Autowired
    private List<SatelliteRequestData> satellites;

    public SatelliteRequestDataList() {
        satellites = new ArrayList<>();
    }

    public List<SatelliteRequestData> getSatellites() {
        return satellites;
    }

    public List<List<String>> getMessages() {
        final List<List<String>> messages = new ArrayList<>();
        satellites.forEach(satellite -> messages.add(satellite.getMessage()));
        return messages;
    }

    public float[] getDistances() {
        final float[] distances = new float[satellites.size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = satellites.get(i).getDistance();
        }
        return distances;
    }
}
