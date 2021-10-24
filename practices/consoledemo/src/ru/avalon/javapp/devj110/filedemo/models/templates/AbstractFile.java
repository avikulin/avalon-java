package ru.avalon.javapp.devj110.filedemo.models.templates;

import ru.avalon.javapp.devj110.filedemo.models.enums.FileFormats;

public abstract class AbstractFile {
    private String fileName;
    private int sizeInBytes;
    private FileFormats formatId;

    public AbstractFile(String fileName, FileFormats formatId, int sizeInBytes) {
        setFileName(fileName);
        setSizeInBytes(sizeInBytes);
        setFormatId(formatId);
    }

    public void setFileName(String fileName) {
        if ((fileName==null)||(fileName.isEmpty())){
            throw new IllegalArgumentException("Filename must be set to non-empty string");
        }
        this.fileName = fileName;
    }

    public void setSizeInBytes(int sizeInBytes) {
        if (sizeInBytes < 0){
            throw new IllegalArgumentException("Size must be a positive value");
        }
        this.sizeInBytes = sizeInBytes;
    }

    public void setFormatId(FileFormats formatId) {
        if (formatId == null){
            throw new IllegalArgumentException("Format must not be null");
        }
        this.formatId = formatId;
    }

    public String getFileName() {
        return fileName;
    }

    public int getSizeInBytes() {
        return sizeInBytes;
    }

    public FileFormats getFormatId() {
        return formatId;
    }

    public abstract void printItem();

    public static void printCollection(AbstractFile[] files){
        // to be done
    }


}
