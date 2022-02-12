package ru.avalon.javapp.devj120.avalontelecom.enums;

public enum ClientType {
    PERSON("Individual person", 0b01),
    COMPANY("Company", 0b10);

    private final String typeName;
    private final int mask;

    ClientType(String typeName, int mask) {
        this.typeName = typeName;
        this.mask = mask;
    }

    public int getMask() {
        return mask;
    }

    @Override
    public String toString() {
        return typeName;
    }
}
