package example1;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        Warehouse w = new Warehouse();
        AbstractEntity[] entities = new AbstractEntity[]{
                new Producer("p1", w),
                new Producer("p2", w),
                new Producer("p3", w),
                new Consumer("c1", w),
                new Consumer("c2", w),
                new Consumer("c3", w),
                new Consumer("c4", w),
                new Consumer("c5", w)
        };

        for (AbstractEntity e : entities){
            e.start();
        }
        entities[0].join();
    }
}
