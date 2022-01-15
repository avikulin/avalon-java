package utils;

import contracts.DiagnosticLogger;
import contracts.Tokenizer;

import java.util.ArrayList;
import java.util.List;

import static constants.Constants.*;

public class SequenceTokenizer implements Tokenizer {
    private char delimiterSymbol;
    private DiagnosticLogger logger;

    public SequenceTokenizer(char delimiterSymbol) {
        this.delimiterSymbol = delimiterSymbol;
        this.logger = Tracer.get();

        logger.logInfo(
                this.getClass(),
                String.format("Instance has been created (delimiter = '%c')",
                        this.delimiterSymbol)
        );
    }

    @Override
    public void setDelimiterSymbol(char symbol) {
        this.delimiterSymbol = symbol;
        logger.logInfo(
                this.getClass(),
                String.format("New delimiter symbol has been set: '%c'", delimiterSymbol)
        );
    }

    @Override
    public char getCurrentDelimiter() {
        return this.delimiterSymbol;
    }

    @Override
    public Object[] tokenize(String source) throws IllegalArgumentException {
        if (source == null || source.isEmpty()) {
            String msg = "Source string must be not-null and non-empty";
            IllegalArgumentException e = new IllegalArgumentException(msg);
            logger.logError(this.getClass(), e, "Invalid input parameter", msg);
            throw e;
        }

        List<String> res = new ArrayList<>();
        int sourceLength = source.length() - 1;
        int quotesMatcher = 0;
        int nextStartPos = 0;

        int numberOfTokens = 0;
        for (int i = 0; i < source.length(); i++) {
            char ch = source.charAt(i);
            if (ch == SEPARATOR_QUOTE_SYMBOL) {
                quotesMatcher++;
            }
            if (ch == this.delimiterSymbol) {
                //не учитываем запятаи внутри литералов
                if (quotesMatcher % 2 == 0) {
                    String literal = source.substring(nextStartPos, i);
                    if (literal.isEmpty()) {
                        literal = DEFAULT_CELL_VALUE;
                    } else if (literal.charAt(0) == SEPARATOR_QUOTE_SYMBOL) { // quotes unpack
                        String quote = Character.toString(SEPARATOR_QUOTE_SYMBOL);
                        literal = literal.substring(1, literal.length() - 1).replace(SEPARATOR_DOUBLE_QUOTE, quote);
                    }

                    res.add(literal);
                    numberOfTokens++;
                    //запоминаем начальную позицию токена для сл. итерации
                    nextStartPos = Math.min(i + 1, sourceLength);
                }
            }
        }
        if (quotesMatcher % 2 != 0) {
            String msg = String.format("String has incorrect CSV-format : \"%s\"", source);
            IllegalArgumentException e = new IllegalArgumentException(msg);
            logger.logError(this.getClass(), e, "Invalid input parameter", msg);
            throw e;
        }

        if (numberOfTokens == 0) { //there is no commas
            res.add(source);
        }

        return res.toArray();
    }
}
