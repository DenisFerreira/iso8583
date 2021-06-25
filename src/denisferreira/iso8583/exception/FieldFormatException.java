package denisferreira.iso8583.exception;

public class FieldFormatException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = 8537914128853788143L;

    public FieldFormatException(int bit, int format, String value) {
        super(String.format("bit[%d]: format is %d, actual value is %s", bit, format, value));
    }
}
