package denisferreira.iso8583;

import java.util.Random;

public class IsoCounter {
    private int base;

    public IsoCounter() {
        Random random = new Random();
        this.base = random.nextInt(90000);
    }

    public String getCounter() {
        base++;
        return String.format("%06d", base);
    }
}
