package example1;

public class Warehouse {
    private int amount;
    private Object semaphore = new Object();

    public void storeGoods(Producer p, int amount, int loadTime){
        System.out.printf("%s wants to load %d units of goods. %n", p.getName(), amount);
        try {
            Thread.sleep(loadTime);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        synchronized (this) {
            this.amount += amount;
            notifyAll();
            System.out.printf("%s loaded its goods. Balance now is %d.%n", p.getName(), this.amount);
        }
    }

    public void consumeGoods(Consumer c, int amount, int consumeTime) {
        System.out.printf("%s wants to take %d units of goods. %n", c.getName(), amount);

        synchronized (this) {
            if (this.amount < amount) {
                try {
                    wait();
                } catch (InterruptedException e) {
                }
            }
            this.amount -= amount;
            try {
                Thread.sleep(consumeTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.printf("%s has taken its goods. Balance now is %d.%n", c.getName(), this.amount);
        }
    }


}
