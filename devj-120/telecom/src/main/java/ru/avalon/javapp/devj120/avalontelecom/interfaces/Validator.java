package ru.avalon.javapp.devj120.avalontelecom.interfaces;

import java.awt.*;

public interface Validator {
    void registerSource(TextComponent component);
    boolean getValidationState();
    String getErrorState();
}
