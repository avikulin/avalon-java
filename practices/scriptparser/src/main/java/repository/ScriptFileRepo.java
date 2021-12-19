package repository;

import contracts.dal.SourceRepo;

import java.io.*;
import java.util.Iterator;
import java.util.function.Consumer;

public class ScriptFileRepo implements SourceRepo {
    private BufferedReader source;
    private int lineLastRead;
    @Override
    public void LoadFile(File file) throws NullPointerException, IOException {
        if (file == null){
            throw new NullPointerException("File reference must be set");
        }
        lineLastRead = 0;
        source = new BufferedReader(new FileReader(file));
    }

    @Override
    public int getReadPosition() {
        return lineLastRead;
    }

    @Override
    public Iterator<String> iterator() {
        return new Iterator<String>() {
            private boolean hasNext = true;
            @Override
            public boolean hasNext() {
                return hasNext;
            }

            private String getLine() throws IOException {
                String readBuffer;
                readBuffer = source.readLine();
                if (readBuffer != null){
                    lineLastRead++;
                    return readBuffer.trim();
                } else {
                    hasNext = false;
                    return null;
                }
            }

            @Override
            public String next() {
                String res;
                try {
                    res = getLine();
                } catch (IOException e) {
                    return null;
                }
                return res;
            }
        };
    }

    @Override
    public void forEach(Consumer<? super String> action) {
        for(String s: this){
            action.accept(s);
        }
    }
}
