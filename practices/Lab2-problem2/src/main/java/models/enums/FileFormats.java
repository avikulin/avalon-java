package models.enums;

public enum FileFormats {
    DOCX("*.docx", FileType.DOCUMENT, "ms word document"),
    XLSX("*.xlsx", FileType.DOCUMENT, "ms excel table"),
    PNG("*.png", FileType.IMAGE, "image"),
    JPG("*.jpg", FileType.IMAGE, "image"),
    MP3("*.mp3", FileType.MULTIMEDIA, "audio"),
    ACC("*.acc", FileType.MULTIMEDIA, "audio"),
    AVI("*.avi", FileType.VIDEO, "video"),
    MP4("*.mp4", FileType.VIDEO, "video");;

    private final String fileExtension;
    private final FileType type;
    private final String contentType;

    FileFormats(String fileExtension, FileType type, String contentType) {
        this.fileExtension = fileExtension;
        this.type = type;
        this.contentType = contentType;
    }

    public FileType getType() {
        return this.type;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", contentType, fileExtension);
    }
}
