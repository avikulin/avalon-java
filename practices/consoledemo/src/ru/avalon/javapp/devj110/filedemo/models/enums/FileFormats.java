package ru.avalon.javapp.devj110.filedemo.models.enums;

public enum FileFormats {
    DOCX("document"),
    MP3("music"),
    PNG("audio"),
    AVI("video");

    String typeDescription;
    FileFormats(String typeDescription){
        this.typeDescription = typeDescription;
    }

    @Override
    public String toString() {
        return typeDescription;
    }
}
