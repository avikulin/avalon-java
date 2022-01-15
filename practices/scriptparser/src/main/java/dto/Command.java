package dto;

import contracts.dataobjects.Statement;
import contracts.dataobjects.Expression;
import enums.CommandType;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringJoiner;

import static constants.Constants.*;

public class Command implements Statement, Iterable<Expression> {
    private final int id;

    private CommandType type;
    private String lvalue;
    private List<Expression> rvalue;

    public Command(int id, CommandType type, String lvalue)
            throws NullPointerException, IllegalArgumentException {
        this.id = id;
        setType(type);
        setLvalue(lvalue);
        rvalue = new ArrayList<>();
    }

    private void setType(CommandType type) throws NullPointerException {
        if (type == null){
            throw new NullPointerException("Type param must be set");
        }
        this.type = type;
    }

    private void setLvalue(String lvalue) throws IllegalArgumentException {
        if (type==CommandType.SET) {
            if ((lvalue == null || lvalue.isEmpty())) {
                throw new IllegalArgumentException("LValue param must be defined for this command");
            }
            this.lvalue = lvalue;
        } else {
            if (lvalue != null) {
                throw new IllegalArgumentException("This command does not maintain a LValue param");
            }
        }
    }

    public void addRvalue(Expression node) throws IllegalArgumentException {
        if (type==CommandType.SET||type==CommandType.PRINT||type==CommandType.INPUT) {
            if (rvalue == null) {
                throw new IllegalArgumentException("RValue param must be defined for this command");
            }
            this.rvalue.add(node);
        } else {
            throw new IllegalArgumentException("This command does not maintain a RValue param");
        }
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public CommandType getType() {
        return type;
    }

    @Override
    public String getLValue() {
        return lvalue;
    }

    @Override
    public boolean hasLValue() {
        return lvalue != null;
    }

    @Override
    public Expression getRValue(int id) {
        if (id<0||id>rvalue.size()-1){
            throw new IllegalArgumentException("Passed index is out of bounds");
        }
        return rvalue.get(id);
    }

    @Override
    public int getRValueLength() {
        return rvalue.size();
    }

    @Override
    public boolean hasRValue() {
        return rvalue.size() > 0;
    }

    @Override
    public String toString() {
        String res =  type.toString();

        if (hasLValue()){
            res = res.concat(" ").concat(lvalue);
        }
        if (hasLValue()&&hasRValue()) {
            res = res.concat(" ").concat(Character.toString(SEPARATOR_EQUALITY_SYMBOL));
        }

        if (hasRValue()) {
            StringJoiner rvalueStr = new StringJoiner(",");
            for (Expression expression : this) {
                rvalueStr.add(expression.toString());
            }
            res = res.concat(" ").concat(rvalueStr.toString());
        }
        return res;
    }

    @Override
    public Iterator<Expression> iterator() {
        return rvalue.iterator();
    }
}
