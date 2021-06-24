package denisferreira.iso8583;

public class BCD {
	public static byte[] StringToBcd(String str) {
		byte[] buffer = new byte[str.length()];
		byte[] output = new byte[buffer.length / 2];

		System.arraycopy(str.getBytes(), 0, buffer, 0, str.length());

		int j = 0;
		for (int i = 0; i < buffer.length; i++) {
			output[j] = (byte) ((buffer[i] - (buffer[i] >= 0x30 && buffer[i] <= 0x39 ? 0x30 : 0x37) & 0x0F) << 4);
			i++;
			output[j] |= (byte) (buffer[i] - (buffer[i] >= 0x30 && buffer[i] <= 0x39 ? 0x30 : 0x37) & 0x0F);
			j++;
		}
		return output;
	}

	private static final String x = "0123456789ABCDEF";

	public static String BcdToString(byte[] input) {
		StringBuilder temp = new StringBuilder(input.length * 2);
		int index;

		for (byte b : input) {
			index = (b & 0xF0) >>> 4;
			temp.append(x.charAt(index));
			index = b & 0x0F;
			temp.append(x.charAt(index));
		}
		return temp.toString();
	}

	public static String BcdToString(byte input) {
		StringBuilder temp = new StringBuilder(2);
		int index;

		index = (input & 0xF0) >>> 4;
		temp.append(x.charAt(index));
		index = input & 0x0F;
		temp.append(x.charAt(index));

		return temp.toString();
	}
}
