package models.entityclasses;

import models.enums.FileFormats;
import models.enums.FileType;
import models.templateclasses.AbstractFile;

public class DocumentFile extends AbstractFile {
    private int numberOfPages;

    public DocumentFile(String fileName, FileFormats formatId, int sizeInBytes, int numberOfPages)
            throws IllegalStateException {
        super(fileName, formatId, sizeInBytes);
        setNumberOfPages(numberOfPages);
    }

    public int getNumberOfPages() {
        return numberOfPages;
    }

    public void setNumberOfPages(int numberOfPages) throws IllegalStateException {
        if (numberOfPages <= 0) {
            throw new IllegalArgumentException("Number of pages must be positive value over zero");
        }
        this.numberOfPages = numberOfPages;
    }

    @Override
    protected boolean checkFileFormatCorrectness(FileFormats formatId) {
        return formatId.getType() == FileType.DOCUMENT;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        DocumentFile that = (DocumentFile) o;
        return numberOfPages == that.numberOfPages;
    }

    @Override
    public String getDetailedInfo() {
        return String.format("%s, %d pages", this.getFormatId(), this.getNumberOfPages());
    }
}
