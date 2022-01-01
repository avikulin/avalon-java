package interfaces;

public interface DbConnection {
    DbConnection getContext();
    void connectTo(String s);
    boolean isReady();
}
