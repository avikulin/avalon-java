package models.entityclasses;

import models.dto.Dimensions;
import models.dto.Duration;
import models.enums.FileFormats;
import models.enums.FileType;

import java.util.Objects;

public class VideoFile extends MultimediaFile {
    private Dimensions thumbPicDimensions;

    public VideoFile(String fileName, FileFormats formatId, int sizeInBytes, String contentDescription,
                     Duration duration, Dimensions thumbPicDimensions) throws IllegalStateException {
        super(fileName, formatId, sizeInBytes, contentDescription, duration);
        setThumbPicDimensions(thumbPicDimensions);
    }

    @Override
    protected boolean checkFileFormatCorrectness(FileFormats formatId) {
        return formatId.getType() == FileType.VIDEO;
    }

    public void setThumbPicDimensions(Dimensions thumbPicDimensions) throws IllegalStateException {
        if (thumbPicDimensions == null) {
            throw new IllegalArgumentException("Dimensions reference parameter must be not-null");
        }
        this.thumbPicDimensions = thumbPicDimensions;
    }

    public Dimensions getThumbPicDimensions() {
        return thumbPicDimensions;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        VideoFile videoFile = (VideoFile) o;
        return Objects.equals(thumbPicDimensions, videoFile.thumbPicDimensions);
    }

    @Override
    public String getDetailedInfo() {
        return String.format("%s, %s", super.getDetailedInfo(), this.getThumbPicDimensions());
    }
}
