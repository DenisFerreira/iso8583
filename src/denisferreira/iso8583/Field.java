package denisferreira.iso8583;

import java.math.BigInteger;

import denisferreira.iso8583.exception.*;

public class Field {
	public static final int FIXED = 0;
	public static final int LVAR = 1;
	public static final int LLVAR = 2;
	public static final int LLLVAR = 4;

	public static final int LEN_FORMAT = FIXED | LVAR | LLVAR | LLLVAR;

	public static final int NUMERIC = 8;
	public static final int ALPHA = 16;
	public static final int SPECIAL = 32;
	public static final int Z_STRING = 64;
	public static final int BINARY = 128;

	public static final int FIELD_FORMAT = NUMERIC | ALPHA | SPECIAL | Z_STRING | BINARY;
	protected int bit;
	private int format;
	protected int maxLen;
	private String description;

	public Field(int bit, int format, int maxLen, String description) {
		this.setBit(bit);
		this.setFormat(format);
		this.setMaxLen(maxLen);
		this.setDescription(description);
	}

	public Field(int bit, int format, int maxLen) {
		this.setBit(bit);
		this.setFormat(format);
		this.setMaxLen(maxLen);
		this.setDescription("");
	}

	public int getBit() {
		return bit;
	}

	public void setBit(int bit) {
		this.bit = bit;
	}

	public int getFormat() {
		return format;
	}

	public void setFormat(int format) {
		this.format = format;
	}

	public int getMaxLen() {
		return maxLen;
	}

	public void setMaxLen(int maxLen) {
		this.maxLen = maxLen;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public int serializeField(String value, StringBuffer outputValue) {
		if (value.length() > this.maxLen)
			throw new FieldSizeBiggerThanMaxLenghException(this.bit, this.maxLen, value.length());
		switch (this.getFormat() & LEN_FORMAT) {
		case FIXED:
			if (value.length() != this.maxLen)
				throw new FieldSizeSmallerThanExpectedException(this.bit, this.maxLen, value.length());
			switch (this.getFormat() & FIELD_FORMAT) {
			case NUMERIC:
				try {
					outputValue.append(String.format("%0" + getMaxLen() + "d", new BigInteger(value)));
				} catch (NumberFormatException e) {
					throw new FieldFormatException(this.bit, this.format, value);
				}
				break;
			case ALPHA | NUMERIC | SPECIAL:
				outputValue.append(String.format("%-" + getMaxLen() + "s", value));
				break;
			case Z_STRING:
				if (this.maxLen != value.length())
					return -1;
			default:
				outputValue.append(value);
			}
			break;
		case LLVAR:
			String ll = String.format("%02d", value.length());
			outputValue.append(ll);
			outputValue.append(value);
			break;
		case LLLVAR:
			String lll = String.format("%03d", value.length());
			outputValue.append(lll);
			outputValue.append(value);
			break;
		}
		return 0;
	}

	public String unserializeField(String buffer, int offset) {
		int len;
		String value = null;

		if (this.bit == 1)
			return null;

		switch (this.getFormat() & LEN_FORMAT) {
		case FIXED:
			len = this.getMaxLen();
			value = buffer.substring(offset, offset + len);
			break;
		case LLVAR:
			int llSize = Integer.parseInt(buffer.substring(offset, offset + 2));
			value = buffer.substring(offset + 2, offset + llSize + 2);
			break;
		case LLLVAR:
			int lllSize = Integer.parseInt(buffer.substring(offset, offset + 3));
			value = buffer.substring(offset + 3, offset + lllSize + 3);
			break;
		}

		return value;
	}

}
