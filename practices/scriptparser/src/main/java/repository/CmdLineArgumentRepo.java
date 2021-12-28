package repository;

import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.dal.ArgRepo;
import utils.Tracer;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class CmdLineArgumentRepo implements ArgRepo {
    private static CmdLineArgumentRepo obj;
    private final List<String> argValues;

    private CmdLineArgumentRepo() {
        argValues = new ArrayList<>();
    }

    public static CmdLineArgumentRepo get() {
        if (obj == null) {
            obj = new CmdLineArgumentRepo();
        }
        return obj;
    }

    @Override
    public void putValue(String value) {
        argValues.add(value);
    }

    @Override
    public double getById(int id) throws IndexOutOfBoundsException {
        if (id < 0 || id > argValues.size() - 1) {
            throw new IndexOutOfBoundsException("Index parameter is out of bounds");
        }
        String stringValue = argValues.get(id);
        double doubleValue;
        try {
            doubleValue = Double.parseDouble(stringValue);
        }catch (NumberFormatException exception){
            String msg = String.format("Parameter #%d (\"%s\") can't be represented as decimal value", id, stringValue);
            Tracer.get().logError(this.getClass(), msg);
            throw new IllegalStateException(msg);
        }
        return doubleValue;
    }

    public static Function<Double, Double> getFunctionDelegate(){
        return x-> get().getById(x.intValue());
    }
}
