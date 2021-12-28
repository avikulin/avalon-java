package enums;

public enum TokenType {
    LEFT_BRACKET(false, "Left bracket",null, null, null),
    RIGHT_BRACKET(false, "Right bracket",null, null, null),
    STRING_LITERAL(true,"String literal",
            "Sequence of chars between quotes",
               "\"{[char]..}\"",
              "\"literal string\""),
    NUMBER_LITERAL (true,"Number literal",
            "Sequence of digits without delimiters with one decimal point",
               "\"{[0-9]..}.{[0-9]..}\"",
              "235.54"),
    VARIABLE(true,"Variable",
            "Sequence of letters (incl. subscript) and digits without delimiters and spaces followed " +
                    "after the dollar symbol ('$'). First char after '$' should be a letter.",
            "${[char]..}",
            "$variable"),
    FUNCTION(true,"Function sub-expression",
            "String representation of the functional sub-expression according to its format",
            "See separate topic",
            "See separate topic"),
    OPERATION(false,"Arithmetic operation", null, null, null);

    private final boolean representsParameterDataType;
    private final String name;
    private final String description;
    private final String format;
    private final String example;

    TokenType(boolean representsParameterDataType, String name, String description, String format, String example) {
        this.representsParameterDataType = representsParameterDataType;
        this.name = name;
        this.description = description;
        this.format = format;
        this.example = example;
    }

    public static String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Supported data types are:\n");
        for (TokenType tokenType: TokenType.values()){
            if (tokenType.representsParameterDataType){
                builder.append(tokenType.name.toUpperCase());
                builder.append(" : ");
                builder.append(tokenType.description);
                builder.append(". Formatting rule: ");
                builder.append(tokenType.format);
                builder.append(". For example: ");
                builder.append(tokenType.example);
                builder.append(System.lineSeparator());
            }
        }
        return builder.toString();
    }
}
