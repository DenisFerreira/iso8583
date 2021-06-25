package denisferreira.iso8583.exception;

public class FieldLengthException extends RuntimeException {

    /**
     *
     */
    private static final long serialVersionUID = -2341943194240786944L;

    public FieldLengthException(int bit, int maxLen, int valueLen) {
        super(String.format("bit[%d]: max length is %d, actual value size is %d", bit, maxLen, valueLen));
    }
}
