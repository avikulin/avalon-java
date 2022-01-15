package contracts;

import java.io.File;

public interface SourceRepo extends Iterable<String>, AutoCloseable {
    void loadFile(File file);

    int getReadPosition();

    String getFileName();

    boolean isReady();
}
