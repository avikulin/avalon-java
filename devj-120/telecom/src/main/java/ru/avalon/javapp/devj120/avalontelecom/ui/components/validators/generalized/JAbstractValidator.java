package ru.avalon.javapp.devj120.avalontelecom.ui.components.validators.generalized;

import ru.avalon.javapp.devj120.avalontelecom.interfaces.Validator;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.awt.*;

public abstract class JAbstractValidator implements Validator {
    private TextComponent source;
    private String errorState;

    @Override
    public void registerSource(TextComponent component) {
        if (component == null) {
            throw new IllegalArgumentException("Component reference param must be not-null");
        }
        this.source = component;
    }

    protected TextComponent getSource() throws IllegalStateException {
        if(this.source == null){
            throw new IllegalStateException("Validation source not set");
        }
        return this.source;
    }

    protected void setErrorState(String errorState){
        this.errorState = errorState;
    }

    @Override
    public boolean getValidationState() {
        throw new NotImplementedException();
    }

    @Override
    public String getErrorState() {
        return this.errorState;
    }
}
