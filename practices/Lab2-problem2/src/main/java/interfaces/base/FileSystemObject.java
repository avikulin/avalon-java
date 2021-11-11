package interfaces.base;

import models.enums.FileFormats;

public interface FileSystemObject {
    String getFileName();
    int getSizeInBytes();
    FileFormats getFormatId();
}
