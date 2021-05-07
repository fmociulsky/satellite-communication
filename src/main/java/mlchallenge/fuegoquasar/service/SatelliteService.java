package mlchallenge.fuegoquasar.service;

import mlchallenge.fuegoquasar.model.Satelite;
import mlchallenge.fuegoquasar.model.Satellites;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.StringJoiner;
import java.util.stream.Collectors;

public class SatelliteService {
    final List<String> messages = new ArrayList<>();

    public String getMessage(Satellites satellites) {
        initializeMessages();
        buildMergedMessageFromSatellites(satellites.getSatellites());
        if(messages.contains(EMPTY)) return MESSAGE_INCOMPLETE;

        final StringJoiner stringJoiner = new StringJoiner(" ");
        messages.forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    private void buildMergedMessageFromSatellites(List<Satelite> satellites) {
        for (final Satelite satellite : satellites) {
            for (int i = 0; i < satellite.getMessage().size(); i++) {
                if(messages.get(i).equals(EMPTY))
                    messages.set(i,satellite.getMessage().get(i));
            }
        }
    }

    private void initializeMessages() {
        for (int i = 0; i < MESSAGE_LENGHTH; i++) {
            messages.add(EMPTY);
        }
    }

    private static final int MESSAGE_LENGHTH = 5;
    private static final String EMPTY = "";
    private static final String MESSAGE_INCOMPLETE = "The message could't be received completed - Comunication Error";
}
