package model;

import java.io.Serializable;
import java.util.List;



/**
 *  Runs game loop and provides game objects.
 */
public class Game implements Serializable {
    private transient boolean running = false;
    static volatile Currency currency;
    private int interval = 500;
    private List<Producer> producers;
    private Producer manualProducer;
    private List<Upgrade> upgrades;
    private transient Thread thread;

    public Game(){
        currency = new Currency();
        manualProducer = new Producer();
        manualProducer.setName("Button");
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

    public List<Producer> getProducers() {
        return producers;
    }

    public Producer getManualProducer() {
        return manualProducer;
    }

    public List<Upgrade> getUpgrades() {
        return upgrades;
    }

    private void tick(){
        //System.out.println(currency.getAmount());
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
