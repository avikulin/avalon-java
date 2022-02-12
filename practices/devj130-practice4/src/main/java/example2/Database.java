package example2;

import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class Database {
    private AtomicInteger readersIn = new AtomicInteger();
    private AtomicBoolean writerIn = new AtomicBoolean();

    private static final int PERMITS = 100;
    private Semaphore semaphore = new Semaphore(PERMITS);

    public void readerEnters(Reader r)  {
        System.out.printf("%s wants to connect to the database.%n", r);
        try {
            semaphore.acquire();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        readersIn.incrementAndGet();
        System.out.printf("%s connected to the database. %d readers in the database now.%n", r, readersIn.get());
    }

    public void readerExits(Reader r) {
        semaphore.release();
        readersIn.decrementAndGet();
        System.out.printf("%s leaves the database.%d readers in the database now.%n", r, readersIn.get());
    }

    public void writerEnters(Writer w){
        System.out.printf("%s wants to connect to the database.%n", w);
        try {
            semaphore.acquire(PERMITS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        writerIn.set(true);
        System.out.printf("%s connected to the database.%n", w);
    }

    public void writerExits(Writer w){
        semaphore.release(PERMITS);
        writerIn.set(false);
        System.out.printf("%s leaves the database.%n", w);
    }
}
