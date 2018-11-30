package model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;



/**
 *  Runs game loop and provides game objects.
 */
public class Game implements Serializable {
    private static transient boolean running = false;
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

    public static boolean isRunning() {
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
        // autoProducers.forEach(Producer::produce);
    }

    private void runLoop() {
        getAutoProducers().forEach(producer -> new Thread(producer).start());
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
        //thread.start();
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
        String[] names = {
                "Hapless Intern",
                "Functional Programmer",
                "Joey Subclass",
                "",
                "",
                ""
        };
        List<Producer> producers = new ArrayList<>();

        producers.add(new Producer(
                .5,
                100.0,
                    1.15,
                    1.1,
                    0,
                    true,
                "Hapless Intern",
                500
        ));

        producers.add(new Producer(
                4,
                500.0,
                1.15,
                1.1,
                0,
                true,
                "Functional Programmer",
                2000));

        producers.add(new Producer(
                10,
                3000.0,
                1.15,
                1.1,
                0,
                true,
                " ",
                10 * 1000));

        producers.add(new Producer(
                40,
                10000.0,
                1.15,
                1.1,
                0,
                true,
                "Joey Subclass",
                100 * 1000)
        );



        return producers;

    }
}