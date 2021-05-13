package mlchallenge.fuegoquasar.service.message;

public class MessageBuilderException extends Exception {
    private static final long serialVersionUID = -1097324298996309034L;
    public MessageBuilderException(MessageBuilderExceptionError message) {
        super(message.getValue());
    }

    public enum MessageBuilderExceptionError {

        MESSAGE_NOT_COMPLETED("Message could not be completed"),
        NO_MESSAGE_SENT("No messages sent");

        private final String value;

        MessageBuilderExceptionError(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
