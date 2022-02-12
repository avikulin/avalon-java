package example2;

public class Writer extends AbstractEntity{
    public Writer(String name, Database database) {
        super(name, database);
    }

    @Override
    public void run() {
        while (true){
            database.writerEnters(this);
            emulateWork(1_000, 1_000);
            database.writerExits(this);
            emulateWork(3_000, 5_000);
        }
    }
}
