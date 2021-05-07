package mlchallenge.fuegoquasar.controller;

import mlchallenge.fuegoquasar.model.Position;

public class HansoloControlleResponse {

    private Position position;
    private String message;


    public HansoloControlleResponse() {
        message = "";
    }

    public HansoloControlleResponse(Position position, String message) {
        this.position = position;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Position getPosition() {
        return position;
    }

    public void setPosition(Position position) {
        this.position = position;
    }
}
