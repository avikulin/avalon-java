package repository;

import dto.BitPosition;
import interfaces.BooleanRepository;

import java.util.StringJoiner;

public class Repository implements BooleanRepository {
    private final int[] storage;
    private final int bitSequenceLength;

    public Repository(int sizeInBits) {
        if (sizeInBits < 1) {
            throw new IllegalArgumentException("Size must be at least 1");
        }
        BitPosition bp = new BitPosition(sizeInBits);
        int numberOfCells = bp.getMajor() + (bp.getMinor() > 0 ? 1 : 0);
        this.storage = new int[numberOfCells];
        this.bitSequenceLength = sizeInBits;
    }

    public Repository(int[] source) {
        if ((source == null) || (source.length == 0)) {
            throw new IllegalArgumentException("Passed array reference must be not-null and points to non-empty array");
        }
        this.storage = source;
        this.bitSequenceLength = source.length * (BitPosition.MSB);
    }

    public int getBitSequenceLength() {
        return bitSequenceLength;
    }

    private void checkBounds(int idx) throws IllegalArgumentException {
        if ((idx < 0) || (idx > bitSequenceLength - 1)) {
            throw new IllegalArgumentException("Index is out of bounds");
        }
    }

    public boolean checkElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        BitPosition bp = new BitPosition(idx);
        return ((storage[bp.getMajor()] & bp.getMask()) != 0);
    }

    public void setElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        BitPosition bp = new BitPosition(idx);
        storage[bp.getMajor()] |= bp.getMask();
    }

    public void put(int idx, boolean value) throws IllegalArgumentException {
        checkBounds(idx);
        if (value) {
            setElement(idx);
        } else {
            unsetElement(idx);
        }
    }

    public void unsetElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        BitPosition bp = new BitPosition(idx);
        storage[bp.getMajor()] &= ~bp.getMask();
    }

    public void invertElement(int idx) throws IllegalArgumentException {
        checkBounds(idx);
        BitPosition bp = new BitPosition(idx);
        storage[bp.getMajor()] ^= bp.getMask();
    }

    public int countTrueElements() {
        int res = 0;
        int processedBitLength = 0;
        for (int byteId = 0; byteId < storage.length; byteId++) {
            int mask = 0b1;
            for (int bitId = 0; bitId < 31; bitId++) {
                if (processedBitLength == bitSequenceLength) {
                    break;
                }
                res += (storage[byteId] & mask) != 0 ? 1 : 0;
                mask = mask << 1;
                processedBitLength++;
            }
        }
        return res;
    }

    @Override
    public String toString() {
        StringJoiner res = new StringJoiner(", ", "[", "]");
        int processedBitLength = 0;
        for (int byteId = 0; byteId < storage.length; byteId++) {
            int mask = 0b1;
            for (int bitId = 0; bitId < 31; bitId++) {
                if (processedBitLength == bitSequenceLength) {
                    break;
                }
                res.add((storage[byteId] & mask) != 0 ? "1" : "0");
                mask = mask << 1;
                processedBitLength++;
            }
        }
        return res.toString();
    }
}
