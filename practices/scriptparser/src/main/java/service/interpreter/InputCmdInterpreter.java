package service.interpreter;

import contracts.dataobjects.Statement;

import java.io.BufferedReader;
import java.io.BufferedWriter;

public class InputCmdInterpreter extends BaseCmdInterpreterImpl {
    private BufferedWriter outputContext;
    private BufferedReader inputContext;

    @Override
    public void registerInputContext(BufferedReader inputContext) {
        if (inputContext == null) {
            throw new NullPointerException("Input context object param must be not null");
        }
        this.inputContext = inputContext;
    }

    @Override
    public void registerOutputContext(BufferedWriter outputContext) {
        if (outputContext == null) {
            throw new NullPointerException("Output context object param must be not null");
        }
        this.outputContext = outputContext;
    }

    @Override
    public void interpretCommand(Statement cmd) {

    }
}
