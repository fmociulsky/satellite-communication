package mlchallenge.fuegoquasar.service.position;

public class PositionException extends RuntimeException {
    private static final long serialVersionUID = -2466133196891496320L;

    protected PositionException(PositionException.PositionExceptionError message) {
        super(message.getValue());
    }
    public PositionException(String message) {
        super(message);
    }

    public enum PositionExceptionError {
        NO_SATELLITE_POSITION("No position registered for satellite %s");

        private final String value;
        PositionExceptionError(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
