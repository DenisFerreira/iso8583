package denisferreira.iso8583.exception;

public class FieldKeyNotFoundException extends RuntimeException {
    /**
     *
     */
    private static final long serialVersionUID = -1580089226089923171L;

    public FieldKeyNotFoundException(Integer key) {
        super(String.format("bit[%d] not found in bitmap.", key));
    }
}
