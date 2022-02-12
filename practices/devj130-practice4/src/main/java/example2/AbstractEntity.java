package example2;

import java.util.Random;

public class AbstractEntity extends Thread{
    protected final Database database;
    protected final Random random = new Random();

    public AbstractEntity(String name, Database database) {
        super(name);
        this.database = database;
    }

    protected void emulateWork(int minMs, int maxMs){
        try {
            this.sleep((long) (minMs + random.nextFloat()*(maxMs - minMs)));
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    @Override
    public String toString() {
        return this.getName();
    }
}
