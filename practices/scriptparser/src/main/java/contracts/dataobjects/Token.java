package contracts.dataobjects;

import enums.TokenType;

public interface Token {
    TokenType getType();
    String getSource();
    int getGroup();
}
