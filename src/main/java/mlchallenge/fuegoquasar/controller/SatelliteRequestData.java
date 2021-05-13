package mlchallenge.fuegoquasar.controller;

import java.util.List;

public class SatelliteRequestData {
    private String name;
    private float distance;
    private List<String> message;

    public SatelliteRequestData(String name, Long distance, List<String> message) {
        this.name = name;
        this.distance = distance;
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getDistance() {
        return distance;
    }

    public List<String> getMessage() {
        return message;
    }

    public void setMessage(List<String> message) {
        this.message = message;
    }
}
