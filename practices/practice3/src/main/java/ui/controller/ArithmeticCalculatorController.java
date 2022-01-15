package ui.controller;

import ui.view.ViewMode;

public interface ArithmeticCalculatorController {
    ViewMode getMode();
    void changeMode(ViewMode mode);
    int getContext();
    
}
