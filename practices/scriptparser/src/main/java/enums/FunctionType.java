package enums;

import java.util.function.Function;

import static constants.Constants.*;

public enum FunctionType {
    EXP(FUNCTION_LITERAL_EXP, Math::exp),
    LN( FUNCTION_LITERAL_LN, Math::log),
    TAN(FUNCTION_LITERAL_TAN, Math::tan),
    CTG(FUNCTION_LITERAL_CTG, x->1/Math.tan(x)),
    SIN(FUNCTION_LITERAL_SIN, Math::sin),
    COS(FUNCTION_LITERAL_COS, Math::cos),
    NOT_A_FUNCTION("", null);

    private String displayName;
    private Function<Double, Double> delegate;

    FunctionType(String displayName, Function<Double, Double> delegate) {
        this.displayName = displayName;
        this.delegate = delegate;
    }

    public String getDisplayName(){return displayName;}
    public Function<Double, Double> getDelegate(){return delegate;}

    public static int getDisplayNameLength(){return FUNCTION_LITERAL_LENGTH;}

    public static FunctionType fromString(String s){
        switch (s){
            case FUNCTION_LITERAL_EXP: return EXP;
            case FUNCTION_LITERAL_LN : return LN;
            case FUNCTION_LITERAL_TAN: return TAN;
            case FUNCTION_LITERAL_CTG: return CTG;
            case FUNCTION_LITERAL_SIN: return SIN;
            case FUNCTION_LITERAL_COS: return COS;
            default: return NOT_A_FUNCTION;
        }
    }

    @Override
    public String toString() {
        return displayName;
    }
}
