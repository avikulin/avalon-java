package constants;

public class Constants {
    public static final char SEPARATOR_OPEN_BRACKET = '(';
    public static final char SEPARATOR_CLOSE_BRACKET = ')';
    public static final char SEPARATOR_VARIABLE_SYMBOL = '$';
    public static final char SEPARATOR_QUOTE_SYMBOL = '\"';
    public static final char SEPARATOR_EQUALITY_SYMBOL = '=';
    public static final char SEPARATOR_COMMA_SYMBOL = ',';

    public static final char OPERATION_SYMBOL_SUM = '+';
    public static final char OPERATION_SYMBOL_SUB = '-';
    public static final char OPERATION_SYMBOL_MUL = '*';
    public static final char OPERATION_SYMBOL_DIV = '/';

    public static final int FUNCTION_LITERAL_LENGTH = 3;
    public static final int OPERATION_LITERAL_LENGTH = 1;
    public static final int VARIABLE_LITERAL_LENGTH = 1;

    public static final String FUNCTION_LITERAL_EXP = "exp";
    public static final String FUNCTION_LITERAL_LN = "log";
    public static final String FUNCTION_LITERAL_TAN = "tan";
    public static final String FUNCTION_LITERAL_CTG = "ctg";
    public static final String FUNCTION_LITERAL_SIN = "sin";
    public static final String FUNCTION_LITERAL_COS = "cos";

    public static final String COMMAND_LITERAL_SET = "set";
    public static final String COMMAND_LITERAL_PRINT = "print";
    public static final String COMMAND_LITERAL_INPUT = "input";
    public static final String COMMAND_LITERAL_TRACE = "trace";
    public static final String COMMAND_LITERAL_COMMENT = "#";

    public static final int RESULT_NOT_FOUND = -1;
}
