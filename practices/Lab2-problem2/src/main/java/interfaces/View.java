package interfaces;

import interfaces.base.FileSystemObject;

public interface View {
    void registerSource(Repository<FileSystemObject> repository) throws IllegalArgumentException;
    void show();
}
