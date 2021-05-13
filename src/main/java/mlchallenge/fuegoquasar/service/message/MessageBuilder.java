package mlchallenge.fuegoquasar.service.message;

import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.MESSAGE_NOT_COMPLETED;
import static mlchallenge.fuegoquasar.service.message.MessageBuilderException.MessageBuilderExceptionError.NO_MESSAGE_SENT;

@Component
public class MessageBuilder {

    private String[] messagePositions = null;
    HashMap<String, List<String>> messagesProcessed;

    public MessageBuilder() {
        messagesProcessed = new HashMap<String, List<String>>();
    }

    public String[] getMessagePositions() {
        return messagePositions;
    }

    public String buildMessage() throws MessageException{
        try {
        if(messagePositions == null || Arrays.stream(messagePositions).allMatch(""::equals)) throw new MessageBuilderException(NO_MESSAGE_SENT);
        final int firstPosition = getFirstPosition();
        validateMessage(firstPosition);
        } catch (final MessageBuilderException e) {
            throw new MessageException(e.getMessage());
        }

        return String.join(" ", messagePositions).trim();
    }

    private int getFirstPosition() {
        int pos = 0;
        while(pos < messagePositions.length &&messagePositions[pos].isEmpty()){
            pos++;
        }
        return pos;
    }

    private void validateMessage(int firstPosition) throws MessageBuilderException {
        for (int i = firstPosition; i < messagePositions.length; i++) {
            if(messagePositions[i].isEmpty()) throw new MessageBuilderException(MESSAGE_NOT_COMPLETED);
        }

    }

    public void processMessage(List<String> message) {
        int stepBackward = 0;
        while(stepBackward < message.size()){
            updatePosition(message, stepBackward);
            ++stepBackward;
        }
    }



    private void updatePosition(List<String> message, int stepBackward) {
        final int messageSteps = messagePositions.length  - 1;
        final String messagePosition = messagePositions[messageSteps - stepBackward];
        if(messagePosition == null || messagePosition.isEmpty())
            messagePositions[messageSteps - stepBackward] = message.get(message.size() - 1 - stepBackward);
    }

    public void processMessages(HashMap<String, List<String>> messagesReceived) {
            buildMessagePositions(messagesReceived);
            messagesProcessed.forEach((key, value)-> processMessage(value));
    }

    private void buildMessagePositions(HashMap<String, List<String>> messagesReceived) {
        messagesReceived.forEach((key, value) -> messagesProcessed.put(key, value));
        final int messageLength = messagesProcessed.values().stream().mapToInt(List::size).max().getAsInt();
        messagePositions = new String[messageLength];
    }
}
