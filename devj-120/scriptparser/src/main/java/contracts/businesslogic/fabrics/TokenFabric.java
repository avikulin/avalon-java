package contracts.businesslogic.fabrics;

import contracts.dataobjects.Token;

public interface TokenFabric {
    Token createLBracketToken(String source);

    Token createRBracketToken(String source);

    Token createStringLiteralToken(String source);

    Token createNumberLiteralToken(String source);

    Token createVariableToken(String source);

    Token createFunctionToken(String source);

    Token createOperationToken(String source);

    Token detect(String source);
}
