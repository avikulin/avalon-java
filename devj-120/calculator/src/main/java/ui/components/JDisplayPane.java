package ui.components;

import enums.CalculationMode;
import enums.Digit;
import utils.Tracer;
import utils.ValueConverter;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigInteger;
import java.net.URL;

public class JDisplayPane extends JLabel {
    private static final String LCD_FONT_NAME = "lcd.ttf";
    private static final String FONT_RESOURCE_DIR = "/fonts/";
    private static final String BLANK_STATE = "0";
    private static final String OVERFLOW_STATE = "DISPLAY OVERFLOW";
    private static final int MAX_DIGITS_ON_DISPLAY = 18;
    private static final String ZERO_VALUE = "0";

    private CalculationMode mode;
    private String displayValue;
    private boolean isInInitialState;
    private int numberOfDigits;

    public JDisplayPane() {
        super();
        mode = CalculationMode.DEC;

        Font lcdFont;
        String lcdFontFileName = FONT_RESOURCE_DIR.concat(LCD_FONT_NAME);

        try {
            InputStream fontIs = this.getClass().getResourceAsStream(lcdFontFileName);
            lcdFont = Font.createFont(Font.TRUETYPE_FONT, fontIs);
        } catch (FontFormatException e) {
            String msg = "Incorrect resource file format : ".concat(LCD_FONT_NAME);
            IllegalStateException exception = new IllegalStateException(msg);
            Tracer.get().logError(this.getClass(), exception, "Font load error");
            throw exception;
        } catch (IOException e) {
            URL path = this.getClass().getResource(lcdFontFileName);
            String strException = (path == null) ? String.format("No such resource found (%s)", lcdFontFileName) : path.getPath();
            String msg = "Resource not found exception : ".concat(strException);
            IllegalStateException exception = new IllegalStateException(msg);
            Tracer.get().logError(this.getClass(), exception, "Font load error");
            throw exception;
        }

        setFont(lcdFont.deriveFont(48.0f));
        Border border = BorderFactory.createEtchedBorder();
        Border padding = new EmptyBorder(5,5,2,10);
        setBorder(new CompoundBorder(border, padding));
        setHorizontalAlignment(RIGHT);
        setVerticalAlignment(BOTTOM);
        setBackground(Color.CYAN);
        setOpaque(true);

        reset();
        Tracer.get().logInfo(this.getClass(), "Instance created");
    }

    public void processDigit(Digit digit) {
        if (digit == null){
            String msg = "Digit reference param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Processing digit error");
            throw exception;
        }

        if (!isInInitialState && numberOfDigits <= MAX_DIGITS_ON_DISPLAY) {
            displayValue = displayValue + digit.getSymbol();
            numberOfDigits++;
        }

        if (digit != Digit.DIGIT_0 && isInInitialState) {
            displayValue = Character.toString(digit.getSymbol());
            isInInitialState = false;
        }

        String s = String.format("%s %s", mode, displayValue);
        setText(s);

        Tracer.get().logInfo(this.getClass(), "Digit processed", digit.toString());
    }

    public void displayValue(String value, boolean allowEdit) {
        if (value == null || value.isEmpty()){
            String msg = "Value string reference param must be not null and not-empty";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in receiving value to display");
            throw exception;
        }

        this.displayValue = value;
        this.isInInitialState = value.equals(ZERO_VALUE) || !allowEdit;
        if (displayValue.length() > MAX_DIGITS_ON_DISPLAY){
            setText(OVERFLOW_STATE);
        } else {
            setText(String.format("%s %s", mode, displayValue));
        }
        Tracer.get().logInfo(this.getClass(), "Value transferred to display", value);
    }

    public String getDisplayValue(){
        return displayValue;
    }

    public boolean isModifiedState(){
        return !isInInitialState;
    }

    public void setMode(CalculationMode newMode){
        if (mode == null){
            String msg = "Calculation mode reference param must be not null";
            IllegalArgumentException exception = new IllegalArgumentException(msg);
            Tracer.get().logError(this.getClass(),exception, "Error in changing mode");
            throw exception;
        }

        BigInteger value = ValueConverter.fromString(displayValue, this.mode);
        this.mode = newMode;
        String s = ValueConverter.toString(value, this.mode);
        displayValue(s, true);
        this.isInInitialState = displayValue.equals(BLANK_STATE);
        Tracer.get().logInfo(this.getClass(), "Display mode changed to", mode.toString());
    }

    public void reset() {
        displayValue(BLANK_STATE, false);
        numberOfDigits = 1;
        Tracer.get().logInfo(this.getClass(), "Display state has been re-initialized");
    }
}
