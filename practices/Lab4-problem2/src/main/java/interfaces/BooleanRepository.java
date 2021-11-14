package interfaces;

public interface BooleanRepository {
    boolean checkElement(int idx) throws IllegalArgumentException;

    int getBitSequenceLength();

    void setElement(int idx) throws IllegalArgumentException;

    void put(int idx, boolean value) throws IllegalArgumentException;

    void unsetElement(int idx) throws IllegalArgumentException;

    void invertElement(int idx) throws IllegalArgumentException;

    int countTrueElements();

    String toString();
}
