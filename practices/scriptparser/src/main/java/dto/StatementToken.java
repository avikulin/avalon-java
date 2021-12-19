package dto;

import contracts.dataobjects.Token;
import enums.TokenType;

public class StatementToken implements Token {
    private TokenType type;
    private String sourceText;

    public StatementToken(TokenType type, String sourceText) {
        setType(type);
        setSourceText(sourceText);
    }

    private void setType(TokenType type) {
        if (type == null) {
            throw new NullPointerException("Type param must be set");
        }
        this.type = type;
    }

    private void setSourceText(String sourceText) {
        if (sourceText == null || sourceText.isEmpty()) {
            throw new NullPointerException("Source text param must be not-null and non-empty string");
        }
        this.sourceText = sourceText;
    }

    @Override
    public TokenType getType() {
        return type;
    }

    @Override
    public String getSource() {
        return sourceText;
    }

    @Override
    public int getGroup() {
        return 0;
    }

    @Override
    public String toString() {
        return "{" + type + ", " + sourceText + '}';
    }
}
