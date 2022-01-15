package contracts.dataobjects;

import enums.CommandType;

public interface Statement extends Iterable<Expression> {
    int getId();
    CommandType getType();
    String getLValue();
    boolean hasLValue();
    void addRvalue(Expression node);
    Expression getRValue(int id);
    int getRValueLength();
    boolean hasRValue();
}
