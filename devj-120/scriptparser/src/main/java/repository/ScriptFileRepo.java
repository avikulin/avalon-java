package repository;

import contracts.businesslogic.utils.DiagnosticLogger;
import contracts.dal.SourceRepo;
import utils.Tracer;

import java.io.*;
import java.util.Iterator;
import java.util.function.Consumer;

public class ScriptFileRepo implements SourceRepo {
    private BufferedReader source;
    private String fileName;
    private int lineLastRead;
    private DiagnosticLogger logger;

    public ScriptFileRepo() {
        logger = Tracer.get();
    }

    @Override
    public void loadFile(File file) {
        fileName = file.getAbsolutePath();

        if (file == null){
            throw new NullPointerException("File reference must be set");
        }
        if (!file.exists() || file.isDirectory()) {
            String msg = String.format("Invalid filename passed: %s", fileName);
            logger.logError(this.getClass(), msg);
            throw new IllegalArgumentException(msg);
        }

        lineLastRead = 0;
        try {
            source = new BufferedReader(new FileReader(file));
        }catch (FileNotFoundException e){
            logger.logError(this.getClass(),e,"Error in setting file input");
        }

    }

    @Override
    public int getReadPosition() {
        return lineLastRead;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    @Override
    public boolean isReady() {
        return source != null;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private boolean hasNext = true;
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            private String getLine() {
                String readBuffer;
                try {
                    readBuffer = source.readLine();
                    if (readBuffer != null) {
                        lineLastRead++;
                        return readBuffer.trim();
                    } else {
                        hasNext = false;
                        return null;
                    }
                }catch (IOException exception){
                    String msg = String.format("Error during the file read operation: filename - \"%s\", string #%d",
                            fileName, lineLastRead);
                    logger.logError(this.getClass(), msg);
                    throw new IllegalStateException(msg);
                }
            }

            @Override
            public String next() {
                return getLine();
            }
        };
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        for(String s: this){
            action.accept(s);
        }
    }

    @Override
    public void close() throws IOException {
        source.close();
    }
}
