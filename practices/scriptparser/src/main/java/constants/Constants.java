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

    public static final int OPERATION_LITERAL_LENGTH = 1;

    public static final String FUNCTION_LITERAL_EXP = "exp";
    public static final String FUNCTION_LITERAL_LN  = "ln";
    public static final String FUNCTION_LITERAL_TAN = "tg";
    public static final String FUNCTION_LITERAL_CTG = "ctg";
    public static final String FUNCTION_LITERAL_SIN = "sin";
    public static final String FUNCTION_LITERAL_COS = "cos";
    public static final String FUNCTION_LITERAL_RAD = "rad";
    public static final String FUNCTION_LITERAL_ARG = "arg";

    public static final String COMMAND_LITERAL_SET = "set";
    public static final String COMMAND_LITERAL_PRINT = "print";
    public static final String COMMAND_LITERAL_INPUT = "input";
    public static final String COMMAND_LITERAL_TRACE = "trace";
    public static final String COMMAND_LITERAL_COMMENT = "#comment";
    public static final String COMMAND_LITERAL_HELP = "help";
    public static final String COMMAND_LITERAL_QUITE = "quite";

    public static final int RESULT_NOT_FOUND = -1;

    public static final String CMD_ARG_KEY_FILENAME = "filename";
}
