package dto;

public class BitPosition {
    public static final int MSB = Integer.SIZE - 1;

    private final int major; //big-indian
    private final int minor; //big-indian
    private final int mask;

    public BitPosition(int idx) {
        this.major = idx / MSB;
        this.minor = idx % MSB;
        this.mask = (minor == 0) ? 1 : 1 << minor;
    }

    public int getMajor() {
        return major;
    }

    public int getMinor() {
        return minor;
    }

    public int getMask() {
        return mask;
    }
}
