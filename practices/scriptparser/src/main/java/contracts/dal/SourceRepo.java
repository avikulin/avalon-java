package contracts.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SourceRepo extends Iterable<String>, AutoCloseable{
    void loadFile(File file);
    int getReadPosition();
    String getFileName();
    boolean isReady();
}
