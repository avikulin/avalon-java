package contracts.dal;

public interface ArgRepo {
    void putValue(String value);
    double getById(int id) throws IndexOutOfBoundsException;
}
