package interfaces;

import interfaces.base.FileSystemObject;

import java.util.Iterator;

public interface FilesController {
    void registerModel(Repository repository) throws IllegalArgumentException;
    void registerView(View view) throws IllegalArgumentException;
    void appendData(FileSystemObject element) throws IllegalArgumentException;
    Iterator<FileSystemObject> iterateDataItems();
    void printAll();
}
