package example2;

public class Reader extends AbstractEntity {
    public Reader(String name, Database database) {
        super(name, database);
    }

    @Override
    public void run() {
        while (true){
            database.readerEnters(this);
            emulateWork(500, 1_000);
            database.readerExits(this);
            emulateWork(2_000, 3_000);
        }
    }
}
