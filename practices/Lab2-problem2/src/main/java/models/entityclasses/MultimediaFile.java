package models.entityclasses;

import models.dto.Duration;
import models.enums.FileFormats;
import models.enums.FileType;
import models.templateclasses.AbstractFile;

import java.util.Objects;

public class MultimediaFile extends AbstractFile {
    private String contentDescription;
    private Duration duration;

    public MultimediaFile(String fileName, FileFormats formatId, int sizeInBytes,
                          String contentDescription, Duration duration) throws IllegalStateException {
        super(fileName, formatId, sizeInBytes);
        setContentDescription(contentDescription);
        setDuration(duration);
    }

    public void setContentDescription(String contentDescription) throws IllegalStateException {
        if ((contentDescription == null) || (contentDescription.isEmpty())) {
            throw new IllegalArgumentException("Content description reference param must be not-null and non-empty value");
        }
        this.contentDescription = contentDescription;
    }

    public void setDuration(Duration duration) throws IllegalStateException {
        if (duration == null) {
            throw new IllegalArgumentException("Duration param reference must be not null");
        }
        this.duration = duration;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public Duration getDuration() {
        return duration;
    }

    @Override
    protected boolean checkFileFormatCorrectness(FileFormats formatId) {
        return formatId.getType() == FileType.MULTIMEDIA;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        MultimediaFile that = (MultimediaFile) o;
        return duration.equals(that.duration) &&
                Objects.equals(contentDescription, that.contentDescription);
    }

    @Override
    public String getDetailedInfo() {
        return String.format("%s, %s, %s", this.getFormatId(), this.getContentDescription(), this.getDuration());
    }
}
