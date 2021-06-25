package denisferreira.iso8583;

import denisferreira.iso8583.exception.FieldFormatException;
import denisferreira.iso8583.exception.FieldLengthException;

import java.math.BigInteger;

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
    public static final int X_NUMERIC = 256;

    public static final int FIELD_FORMAT = NUMERIC | ALPHA | SPECIAL | Z_STRING | BINARY | X_NUMERIC;
    protected int bit;
    protected int maxLen;
    private int format;
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

    public void serializeField(String value, StringBuffer outputValue) throws FieldFormatException, FieldLengthException {
        int lenFormat = this.getFormat() & LEN_FORMAT;
        int fieldFormat = this.getFormat() & FIELD_FORMAT;

        switch (lenFormat) {
            case FIXED:
                if (fieldFormat == X_NUMERIC && value.length() != this.maxLen + 1)
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                if (value.length() != this.maxLen)
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                break;
            case LVAR:
                if (value.length() > this.maxLen || value.length() > 9)
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                String l = String.format("%01d", value.length());
                outputValue.append(l);
                break;
            case LLVAR:
                if (value.length() > this.maxLen || value.length() > 99)
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                String ll = String.format("%02d", value.length());
                outputValue.append(ll);
                break;
            case LLLVAR:
                if (value.length() > this.maxLen || value.length() > 999)
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                String lll = String.format("%03d", value.length());
                outputValue.append(lll);
                break;
        }
        switch (fieldFormat) {
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
                outputValue.append(value);
                break;
            case X_NUMERIC:
                if (value.length() != (this.maxLen + 1))
                    throw new FieldLengthException(this.bit, this.maxLen, value.length());
                try {
                    outputValue.append(String.format("%c%0" + getMaxLen() + "d", value.charAt(0), new BigInteger(value.substring(1))));
                } catch (NumberFormatException e) {
                    throw new FieldFormatException(this.bit, this.format, value);
                }

            default:
                outputValue.append(value);
        }
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
