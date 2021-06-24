package denisferreira.iso8583;

import java.text.SimpleDateFormat;
import java.util.Date;

public class IsoDate {
	private final SimpleDateFormat formatDate10 = new SimpleDateFormat("MMddHHmmss");
	private final SimpleDateFormat formatDate6 = new SimpleDateFormat("HHmmss");
	private final SimpleDateFormat formatDate4 = new SimpleDateFormat("MMdd");
	private Date date = new Date();

	public String date10() {
		return formatDate10.format(date);
	}

	public String date6() {
		return formatDate6.format(date);
	}

	public String date4() {
		return formatDate4.format(date);
	}
}
