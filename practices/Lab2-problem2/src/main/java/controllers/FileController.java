package controllers;

import interfaces.base.FileSystemObject;
import interfaces.FilesController;
import interfaces.Repository;
import interfaces.View;

import java.util.Iterator;

public class FileController implements FilesController {
    private Repository<FileSystemObject> repository;
    private View view;

    @Override
    public void registerModel(Repository repository)  throws IllegalStateException {
        if (repository == null) {
            throw new IllegalArgumentException("Repository reference must be set");
        }
        this.repository = repository;
    }

    @Override
    public void registerView(View view)  throws IllegalStateException {
        if (view == null) {
            throw new IllegalArgumentException("View reference must be set");
        }
        this.view = view;
    }

    @Override
    public void appendData(FileSystemObject element)  throws IllegalStateException {
        repository.put(element);
    }

    @Override
    public Iterator<FileSystemObject> iterateDataItems() {
        return repository.iterator();
    }

    @Override
    public void printAll() {
        view.show();
    }
}
