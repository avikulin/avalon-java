package enums;

import static constants.Constants.*;

public enum CommandType {
    SET(COMMAND_LITERAL_SET, "Evaluates expression from the right side of equality " +
                                       "end sets the evaluated value to variable in the left side of equality",
                                "set <variable>=<number literal|variable|expression>",
                               "set $t=$x*21+13*tg($y+1)/15-14*$z/11-ln($y/2)"),

    PRINT(COMMAND_LITERAL_PRINT, "Evaluates params and prints its values to console according " +
                                          "to their ordering",
                                   "print {<string literal|variable>, ...}",
                                  "print \"value is \", $value"),

    INPUT(COMMAND_LITERAL_INPUT, "Reads decimal value from console and sets its value to variable, " +
                                           "passed in the param",
                                    "input <string literal>, <variable>",
                                   "input \"enter value of variable X\", $x"),

    TRACE(COMMAND_LITERAL_TRACE, "Prints content of internal diagnostic log to console",
                                    "trace <no params>",
                                   "trace"),

    COMMENT(COMMAND_LITERAL_COMMENT, "Internal comment string in code. Doesn't affect execution context",
                                        "# <string literal>",
                                       "# just a sample comment"),

    HELP(COMMAND_LITERAL_HELP, "Prints user manual to console",
                                  "help <no params>",
                                 "help"),

    QUITE(COMMAND_LITERAL_QUITE, "Stop execution process and quites",
                                    "quite <no params>",
                                   "quite");

    private final String literalName;
    private final String description;
    private final String format;
    private final String example;

    CommandType(String literalName, String description,  String format, String example) {
        this.literalName = literalName;
        this.description = description;
        this.format = format;
        this.example = example;
    }

    public static CommandType fromString(String s){
        if (s.charAt(0)==COMMAND_LITERAL_COMMENT.charAt(0)){
            return COMMENT;
        }

        switch (s){
            case COMMAND_LITERAL_SET  : return SET;
            case COMMAND_LITERAL_PRINT: return PRINT;
            case COMMAND_LITERAL_INPUT: return INPUT;
            case COMMAND_LITERAL_TRACE: return TRACE;
            case COMMAND_LITERAL_HELP : return HELP;
            case COMMAND_LITERAL_QUITE: return QUITE;
            default:
                throw new IllegalArgumentException("String is not a valid command name");
        }
    }

    @Override
    public String toString() {
        return literalName;
    }

    public static String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Interpreter supports the following list of commands:\n");
        for (CommandType commandType: CommandType.values()){
                builder.append(commandType.literalName.toUpperCase());
                builder.append(" : ");
                builder.append(commandType.description);
                builder.append(". Formatting rule: ");
                builder.append(commandType.format);
                builder.append(". For example: ");
                builder.append(commandType.example);
                builder.append(System.lineSeparator());
        }
        return builder.toString();
    }
}
