package ru.avalon.javapp.devj120.avalontelecom.enums;

import static ru.avalon.javapp.devj120.avalontelecom.constants.Constants.MASK_COMPANY;
import static ru.avalon.javapp.devj120.avalontelecom.constants.Constants.MASK_PERSON;

public enum ClientType {
    PERSON("Individual person", MASK_PERSON),
    COMPANY("Company", MASK_COMPANY);

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
