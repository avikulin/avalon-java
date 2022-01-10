package ru.avalon.javapp.devj120.avalontelecom.interfaces;

import ru.avalon.javapp.devj120.avalontelecom.enums.ClientType;

public interface ClientTypeDependable {
    void setAvailable(boolean available);
    boolean isSuitableForType(ClientType type);
}
