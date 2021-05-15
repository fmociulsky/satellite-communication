package mlchallenge.fuegoquasar.service.position;

public class PositionCalculatorException extends Exception {
    private static final long serialVersionUID = -8288623848871602574L;

    protected PositionCalculatorException(PositionCalculatorExceptionError error) {
        super(error.getValue());
    }

    protected PositionCalculatorException(String error) {
        super(error);
    }


    public enum PositionCalculatorExceptionError {
        SATELLITE_NOT_FOUND("Satellite not found"),
        SATELLITE_QTY_NOT_ENOUGH("Satellite quantity is not enough to calculate the position"),
        SATELLITES_INFO_MISMATCH("Wrong info"),
        INVALID_DISTANCE("Invalide distance value for satellite %s");

        private final String value;
        PositionCalculatorExceptionError(final String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
