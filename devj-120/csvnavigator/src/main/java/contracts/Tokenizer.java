package contracts;

public interface Tokenizer {
    void setDelimiterSymbol(char symbol);
    char getCurrentDelimiter();
    Object[] tokenize(String source) throws IllegalArgumentException;
}
