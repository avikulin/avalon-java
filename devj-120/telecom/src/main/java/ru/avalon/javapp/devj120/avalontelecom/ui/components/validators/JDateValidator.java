package ru.avalon.javapp.devj120.avalontelecom.ui.components.validators;

import ru.avalon.javapp.devj120.avalontelecom.ui.components.validators.generalized.JAbstractValidator;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static ru.avalon.javapp.devj120.avalontelecom.constants.Constants.DATE_STR_FORMAT;

public class JDateValidator extends JAbstractValidator {
    @Override
    public boolean getValidationState() {
        try {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern(DATE_STR_FORMAT);
            LocalDate.parse(getSource().getText(), dateFormat);
        } catch (DateTimeParseException | IllegalStateException exception) {
            setErrorState("Incorrect date value");
            return false;
        }
        return true;
    }
}
