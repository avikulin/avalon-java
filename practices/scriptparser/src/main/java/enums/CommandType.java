package enums;
import static constants.Constants.*;

public enum CommandType {
    SET(COMMAND_LITERAL_SET),
    PRINT(COMMAND_LITERAL_PRINT),
    INPUT(COMMAND_LITERAL_INPUT),
    TRACE(COMMAND_LITERAL_TRACE),
    COMMENT(COMMAND_LITERAL_COMMENT);

    private String literalName;

    CommandType(String literalName) {
        this.literalName = literalName;
    }

    public static CommandType fromString(String s){
        if (s.charAt(0)==COMMAND_LITERAL_COMMENT.charAt(0)){
            return COMMENT;
        }

        switch (s){
            case COMMAND_LITERAL_SET: return SET;
            case COMMAND_LITERAL_PRINT:return PRINT;
            case COMMAND_LITERAL_INPUT:return INPUT;
            case COMMAND_LITERAL_TRACE:return TRACE;
            default:
                throw new IllegalArgumentException("String is not a valid command name");
        }
    }

    @Override
    public String toString() {
        return literalName;
    }
}
