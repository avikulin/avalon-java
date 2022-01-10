package service.interpreter.commands;

import contracts.dataobjects.Statement;
import service.interpreter.commands.base.BaseCmdInterpreterImpl;

public class CommentCmdInterpreter extends BaseCmdInterpreterImpl {
    public CommentCmdInterpreter() {
        super();
    }

    @Override
    public void interpretCommand(Statement cmd) {
        super.interpretCommand(cmd);

        getLoggingContext().logInfo(this.getClass(), "Interpreting command #",
                Integer.toString(cmd.getId()), ": ", cmd.toString());
    }
}
