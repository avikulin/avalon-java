package ru.avalon.javapp.devj120.avalontelecom.ui.components.validators;

import ru.avalon.javapp.devj120.avalontelecom.ui.components.validators.generalized.JAbstractValidator;

public class JNumberValidator extends JAbstractValidator {
    private boolean isDecimal;
    private int maxLength;

    public JNumberValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean getValidationState() {
        try {
            String s = getSource().getText();
            Integer.parseInt(getSource().getText());

            if (maxLength != -1 && s.length() > maxLength) {
                setErrorState("Value has too much digits");
                return false;
            }
        } catch (NumberFormatException | IllegalStateException exception) {
            setErrorState("Incorrect number value");
            return false;
        }
        return true;
    }
}
