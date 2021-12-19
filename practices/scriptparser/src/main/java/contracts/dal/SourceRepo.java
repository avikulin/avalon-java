package contracts.dal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public interface SourceRepo extends Iterable<String>{
    void LoadFile(File file) throws NullPointerException, IOException;
    int getReadPosition();
}
