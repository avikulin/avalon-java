package models.entityclasses;

import models.dto.Dimensions;
import models.enums.FileFormats;
import models.enums.FileType;
import models.templateclasses.AbstractFile;

public class ImageFile extends AbstractFile {
    private Dimensions imageDimensions;

    public ImageFile(String fileName, FileFormats formatId, int sizeInBytes, Dimensions imageDimensions)
            throws IllegalStateException {
        super(fileName, formatId, sizeInBytes);
        setImageDimensions(imageDimensions);
    }

    public void setImageDimensions(Dimensions imageDimensions) throws IllegalStateException {
        if (imageDimensions == null) {
            throw new IllegalArgumentException("Dimensions reference parameter must be not-null");
        }
        this.imageDimensions = imageDimensions;
    }

    public Dimensions getImageDimensions() {
        return imageDimensions;
    }

    @Override
    protected boolean checkFileFormatCorrectness(FileFormats formatId) {
        return formatId.getType() == FileType.IMAGE;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        ImageFile imageFile = (ImageFile) o;
        return imageDimensions.equals(imageFile.imageDimensions);
    }

    @Override
    public String getDetailedInfo() {
        return String.format("%s, %s", this.getFormatId(), this.getImageDimensions());
    }
}
