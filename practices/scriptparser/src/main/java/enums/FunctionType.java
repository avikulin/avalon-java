package enums;

import repository.CmdLineArgumentRepo;

import java.util.function.Function;

import static constants.Constants.*;

public enum FunctionType{
    EXP(FUNCTION_LITERAL_EXP, Math::exp, "Exponential function", "exp($x+1)"),
    LN( FUNCTION_LITERAL_LN, Math::log, "Logarithmic function", "ln(20+$y)"),
    RAD(FUNCTION_LITERAL_RAD, Math::toRadians, "Converts angle value from degrees to radians", "rad(30)"),
    TAN(FUNCTION_LITERAL_TAN, Math::tan, "Trigonometrical tangent function", "tg(5/$z)"),
    CTG(FUNCTION_LITERAL_CTG, x->1/Math.tan(x), "Trigonometrical co-tangent function", "ctg(rad(45))"),
    SIN(FUNCTION_LITERAL_SIN, Math::sin,"Trigonometrical sinus function","sin(rad(60)/2)"),
    COS(FUNCTION_LITERAL_COS, Math::cos, "Trigonometrical cosine function","cos(3*rad(30))"),
    ARG(FUNCTION_LITERAL_ARG, CmdLineArgumentRepo.getFunctionDelegate(), "Gets command line argument by index " +
            "and returns its decimal value (if possible)", "arg($x)"),
    NOT_A_FUNCTION("", null, "Illegal function",null);

    private final String literalName;
    private final Function<Double, Double> delegate;
    private final String description;
    private final String example;

    FunctionType(String literalName, Function<Double, Double> delegate, String description, String example) {
        this.literalName = literalName;
        this.delegate = delegate;
        this.description = description;
        this.example = example;
    }

    public String getLiteralName(){return literalName;}
    public Function<Double, Double> getDelegate(){return delegate;}

    public static FunctionType fromString(String s){
        switch (s){
            case FUNCTION_LITERAL_EXP: return EXP;
            case FUNCTION_LITERAL_LN : return LN;
            case FUNCTION_LITERAL_TAN: return TAN;
            case FUNCTION_LITERAL_CTG: return CTG;
            case FUNCTION_LITERAL_SIN: return SIN;
            case FUNCTION_LITERAL_COS: return COS;
            case FUNCTION_LITERAL_RAD: return RAD;
            case FUNCTION_LITERAL_ARG: return ARG;
            default: return NOT_A_FUNCTION;
        }
    }

    @Override
    public String toString() {
        return literalName;
    }

    public static String getDescription() {
        StringBuilder builder = new StringBuilder();
        builder.append("Interpreter supports the following list of functions:\n");

        String exampleTemplate = "<arg | expression>";
        for (FunctionType func: FunctionType.values()){
            if (func != FunctionType.NOT_A_FUNCTION){
                builder.append(func.literalName.toUpperCase());
                builder.append(" : ");
                builder.append(func.description);
                builder.append(". For example: ");
                builder.append(func.example);
                builder.append(System.lineSeparator());
            }
        }
        builder.append("\nFormatting rule: every function call has to be formatted as a functional sub-expression: " +
                "<function name>(<number literal|variable>)");
        return builder.toString();
    }
}
