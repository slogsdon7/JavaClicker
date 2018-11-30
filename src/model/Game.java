package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *  Runs game loop and provides game objects.
 */
public class Game implements Serializable {
    private transient boolean running = false;
    static volatile Currency currency;
    private int interval = 500;
    private List<Producer> autoProducers;
    private Producer manualProducer;
    private List<Upgrade> upgrades;
    private transient Thread thread;

    public Game(){
        currency = new Currency();
        manualProducer = new Producer();
        manualProducer.setName("Button");
        autoProducers = ProducerFactory.getAutoProducers();

    }

    public Currency getCurrency() {
        return currency;
    }

    public boolean isRunning() {
        return running;
    }

    public int getInterval() {
        return interval;
    }

    public List<Producer> getAutoProducers() {
        return autoProducers;
    }

    public Producer getManualProducer() {
        return manualProducer;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    private void tick(){
        autoProducers.forEach(Producer::produce);
    }

    private void runLoop() {
        thread = new Thread(() ->
        {
            while (running) {
                tick();
                try {
                    Thread.sleep((long) interval);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }

        });
        thread.start();
    }

    /**
     * Starts game loop. If the game is already running, it does nothing.
     */
    public void start() {
        if (!running) {
            running = true;
            runLoop();
        }
    }

    /**
     * Stops the game loop.
     */
    public void stop() {
        running = false;
    }

    public void save(){}

    //Will reset the database
    public void hardReset(){
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.start();

    }


}


class ProducerFactory {

    static List<Producer> getAutoProducers() {
        List<Producer> producers = new ArrayList<>();
        for (int i = 1; i < 6; i++) {
            //double baseProduction, double costBase, double costFactor, double scalingFactor, int level, boolean automatic, String name
            Producer producer = new Producer(
                    Math.round(1.0 * ((i * i) * 10)),
                    Math.max(100, 100.0 * ((i - 1) * 10)),
                    1.15,
                    1.1,
                    0,
                    true,
                    "Tier " + (i)
            );
            producers.add(producer);
        }
        return producers;

    }
}