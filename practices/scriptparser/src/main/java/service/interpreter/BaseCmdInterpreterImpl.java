package service.interpreter;

import contracts.businesslogic.DiagnosticLogger;
import contracts.businesslogic.interpreters.CommandInterpreter;
import contracts.businesslogic.interpreters.ExpressionInterpreter;
import utils.Tracer;

import java.util.Map;

public abstract class BaseCmdInterpreterImpl implements CommandInterpreter {
    protected ExpressionInterpreter interpreterContext;
    protected DiagnosticLogger logger;

    public BaseCmdInterpreterImpl() {
        logger = Tracer.get();
    }

    @Override
    public void registerInterpreterContext(ExpressionInterpreter interpreterContext) {
        if (interpreterContext == null) {
            throw new NullPointerException("Interpreter context object param must be not null");
        }
        this.interpreterContext = interpreterContext;
    }
}
