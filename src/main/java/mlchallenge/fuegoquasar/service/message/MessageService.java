package mlchallenge.fuegoquasar.service.message;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class MessageService {

    @Autowired
    MessageBuilder messageBuilder;

    public String getMessage(HashMap<String, List<String>> messages) throws MessageException {
        messageBuilder.processMessages(messages);
        return messageBuilder.buildMessage();
    }

    public String buildMessage() throws MessageException {
        return messageBuilder.buildMessage();
    }

    public void processMessage(String key, List<String> message) {
        final HashMap<String, List<String>> messages = new HashMap<>();
        messages.put(key, message);
        messageBuilder.processMessages(messages);
    }






}
