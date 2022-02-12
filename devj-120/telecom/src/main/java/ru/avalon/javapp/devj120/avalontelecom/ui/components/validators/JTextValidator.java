package ru.avalon.javapp.devj120.avalontelecom.ui.components.validators;

import ru.avalon.javapp.devj120.avalontelecom.ui.components.validators.generalized.JAbstractValidator;

public class JTextValidator extends JAbstractValidator {
    private int maxLength;

    public JTextValidator(int maxLength) {
        this.maxLength = maxLength;
    }

    @Override
    public boolean getValidationState() {
        String s = getSource().getText();
        if ((!s.isEmpty())){
            if ((maxLength != -1)&&(s.length() > maxLength)){
                setErrorState("Value has to much characters");
                return false;
            }
            return true;
        } else {
            setErrorState("Value can't be empty");
            return false;
        }
    }
}
