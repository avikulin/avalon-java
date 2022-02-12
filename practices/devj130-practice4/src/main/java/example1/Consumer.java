package example1;

public class Consumer extends AbstractEntity{
    public Consumer(String name, Warehouse warehouse) {
        super(name, warehouse);
    }

    @Override
    public void run() {
        super.run();
        pause(1_000, 2_000);
        warehouse.consumeGoods(this, 8 + rnd.nextInt(20), random(200, 300));
    }
}
