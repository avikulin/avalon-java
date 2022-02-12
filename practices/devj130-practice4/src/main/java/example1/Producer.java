package example1;

public class Producer extends AbstractEntity{
    public Producer(String name, Warehouse warehouse) {
        super(name, warehouse);
    }

    @Override
    public void run() {
        super.run();
        warehouse.storeGoods(this, 5 + rnd.nextInt(20), random(200, 300));
    }
}
