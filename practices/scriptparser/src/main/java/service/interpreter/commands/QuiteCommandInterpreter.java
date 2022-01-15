package service.interpreter.commands;

import contracts.dataobjects.Statement;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

public class QuiteCommandInterpreter extends BaseCmdInterpreterImpl {
    public QuiteCommandInterpreter() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());
        getLoggingContext().logInfo(this.getClass(), "Command #", Integer.toString(cmd.getId()), " is done");
        System.exit(0);
    }
}
