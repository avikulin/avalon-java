package models.templateclasses;

import interfaces.base.FileSystemObject;
import interfaces.base.Viewable;
import models.enums.FileFormats;

public abstract class AbstractFile implements FileSystemObject, Viewable {
    private String fileName;
    private int sizeInBytes;
    private FileFormats formatId;

    public AbstractFile(String fileName, FileFormats formatId, int sizeInBytes) {
        setFileName(fileName);
        setSizeInBytes(sizeInBytes);
        setFormatId(formatId);
    }

    public void setFileName(String fileName) throws IllegalStateException {
        if ((fileName == null) || (fileName.isEmpty())) {
            throw new IllegalArgumentException("Filename must be not null and non-empty value");
        }
        this.fileName = fileName;
    }

    protected abstract boolean checkFileFormatCorrectness(FileFormats formatId);

    public void setFormatId(FileFormats formatId) throws IllegalStateException {
        if (formatId == null) {
            throw new IllegalArgumentException("Filename must be not null and non-empty value");
        }

        if (!checkFileFormatCorrectness(formatId)) {
            throw new IllegalArgumentException("Wrong type of file has been passed");
        }

        this.formatId = formatId;
    }

    public void setSizeInBytes(int sizeInBytes) throws IllegalStateException {
        if (sizeInBytes < 0) {
            throw new IllegalArgumentException("Filename must be not null and non-empty value");
        }
        this.sizeInBytes = sizeInBytes;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AbstractFile that = (AbstractFile) o;
        return sizeInBytes == that.sizeInBytes &&
                fileName.equals(that.fileName) &&
                formatId == that.formatId;
    }
}
