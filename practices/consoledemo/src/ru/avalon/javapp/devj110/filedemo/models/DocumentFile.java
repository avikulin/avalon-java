package ru.avalon.javapp.devj110.filedemo.models;

import ru.avalon.javapp.devj110.filedemo.models.enums.FileFormats;
import ru.avalon.javapp.devj110.filedemo.models.templates.AbstractFile;

public class DocumentFile extends AbstractFile {
    private int numberOfPages;

    public DocumentFile(String fileName, FileFormats formatId, int sizeInBytes, int numberOfPages) {
        super(fileName, formatId, sizeInBytes);
        this.numberOfPages = numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) {
        if (numberOfPages < 0){
            throw new IllegalArgumentException("Number of pages must be positive");
        }
        this.numberOfPages = numberOfPages;
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    @Override
    public void printItem() {

    }
}
