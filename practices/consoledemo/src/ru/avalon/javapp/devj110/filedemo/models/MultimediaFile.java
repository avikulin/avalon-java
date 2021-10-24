package ru.avalon.javapp.devj110.filedemo.models;

import ru.avalon.javapp.devj110.filedemo.models.enums.FileFormats;
import ru.avalon.javapp.devj110.filedemo.models.templates.AbstractFile;
import ru.avalon.javapp.devj110.filedemo.models.valueObjects.DurationStore;

public class MultimediaFile extends AbstractFile {
    private String contentDescription;
    private DurationStore duration;

    public MultimediaFile(String fileName, FileFormats formatId, int sizeInBytes) {
        super(fileName, formatId, sizeInBytes);
        setContentDescription(contentDescription);
        setDuration(duration);
    }

    public void setContentDescription(String contentDescription) {
        if ((contentDescription == null)||(contentDescription.isEmpty())){
            throw new IllegalArgumentException("Content description must be set to non-empty string");
        }
        this.contentDescription = contentDescription;
    }

    public void setDuration(DurationStore duration) {
        if (duration == null){
            throw new IllegalArgumentException("Duration value-object reference must be set");
        }
        this.duration = duration;
    }

    public String getContentDescription() {
        return contentDescription;
    }

    public DurationStore getDuration() {
        return duration;
    }

    @Override
    public void printItem() {
        //
    }
}
