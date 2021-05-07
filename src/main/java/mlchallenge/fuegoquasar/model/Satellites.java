package mlchallenge.fuegoquasar.model;

import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

public class Satellites {

    @Autowired
    List<Satelite> satellites;

    public Satellites() {
    }

    public Satellites(List<Satelite> satellites) {
        this.satellites = satellites;
    }

    public List<Satelite> getSatellites() {
        return satellites;
    }

    public void setSatellites(List<Satelite> satellites) {
        this.satellites = satellites;
    }

}
