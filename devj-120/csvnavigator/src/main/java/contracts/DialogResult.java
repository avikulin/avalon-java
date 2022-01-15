package contracts;

import enums.DlgResult;

public interface DialogResult {
    DlgResult getResult();
    char getDelimiter();
    boolean isTitleRowPresent();
    int maxColumnsToFit();
}
