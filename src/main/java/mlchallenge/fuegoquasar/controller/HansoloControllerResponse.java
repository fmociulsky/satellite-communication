package mlchallenge.fuegoquasar.controller;

import mlchallenge.fuegoquasar.model.Position;

import java.io.Serializable;

public class HansoloControllerResponse implements Serializable {

    private static final long serialVersionUID = 440713978712988513L;
    private Position position;
    private String message;


    public HansoloControllerResponse() {
        message = "";
    }

    public HansoloControllerResponse(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
    public Position getPosition() {
        return position;
    }
}
