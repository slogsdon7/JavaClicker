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
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private String name;
    private List<Producer> autoProducers;
    private Producer manualProducer;
    private List<Upgrade> upgrades;
    private String description;
    private transient Thread thread;

    public Game(){
        currency = new Currency();
        manualProducer = new Producer();
        manualProducer.setName("Cookie");
        autoProducers = ProducerFactory.getAutoProducers();
        name = "Savegame";

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public static Game create(String name) {
        Game game = new Game();
        game.setName(name);
        DB.insert(game);
        return game;
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

    public void delete() {

    }
    public void save() {
        DB.update(this);
    }

    public static List<Game> getSaves() {
        return DB.getSaves();

    }


    void setManualProducer(Producer manualProducer) {
        this.manualProducer = manualProducer;
    }

    public static void main(String[] args) {
        DB.init();
        List<Game> games = Game.getSaves();
        games.forEach(game -> System.out.println(game.getName())
        );
        /*ProducerFactory.getAutoProducers().forEach(Producer::insert);
        Producer man = new Producer();
        man.setName("Cookie");
        man.insert();*/
        //Game game = Game.create("Test");
        //game.start();

    }

    @Override
    public String toString() {
        return description;
    }
}

//helper class
class ProducerFactory {

    static List<Producer> getAutoProducers() {

        List<Producer> producers = new ArrayList<>();

        producers.add(new Producer(
                .5,
                100.0,
                    1.15,
                    1.1,
                    0,
                    true,
                "Grandma",
                500
        ));

        producers.add(new Producer(
                4,
                500.0,
                1.15,
                1.1,
                0,
                true,
                "Bakery",
                2000));

        producers.add(new Producer(
                10,
                3000.0,
                1.15,
                1.1,
                0,
                true,
                "Farm",
                10 * 1000));

        producers.add(new Producer(
                40,
                10000.0,
                1.15,
                1.1,
                0,
                true,
                "Factory",
                100 * 1000)
        );



        return producers;

    }
}