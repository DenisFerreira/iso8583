
package denisferreira.iso8583.exception;

public class FieldSizeSmallerThanExpectedException extends RuntimeException {
	/**
	 * 
	 */
	private static final long serialVersionUID = -6806986957984119462L;

	public FieldSizeSmallerThanExpectedException(int bit, int maxLen, int length) {
		super(String.format("bit[%d]: max length is %d, actual value size is %d", bit, maxLen, length));
	}
}
