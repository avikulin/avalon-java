package example2;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Database d = new Database();
        AbstractEntity[] entities = new AbstractEntity[]{
                new Writer("w1", d),
                new Writer("w2", d),
                new Writer("w3", d),
                new Reader("r1", d),
                new Reader("r2", d),
                new Reader("r3", d),
                new Reader("r4", d),
                new Reader("r5", d)
        };

        for (AbstractEntity e: entities){
            e.start();
        }

        entities[0].join();
    }
}
