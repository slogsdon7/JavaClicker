package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static model.Game.currency;
import static model.Game.isRunning;

/**
 * Produces currency.
 * Use {@link Game#getManualProducer()} and {@link Game#getAutoProducers()} to obtain instances of these for the current game
 */
public class Producer implements Runnable {
    private double baseProduction = 10;
    private double costBase = 5;
    private double costFactor = 1.15;
    private double scalingFactor = 1.0;
    private volatile double lastProduced;
    private int interval;
    private int level = 1;
    private boolean automatic = false;
    private String name;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    Producer() {


    }

    Producer(double baseProduction, double costBase, double costFactor, double scalingFactor, int level, boolean automatic, String name, int interval) {
        this.baseProduction = baseProduction;
        this.costBase = costBase;
        this.costFactor = costFactor;
        this.scalingFactor = scalingFactor;
        this.level = level;
        this.automatic = automatic;
        this.name = name;
        this.interval = interval;
    }

    /**
     * Does production. Fires property change event with amount produced.
     * Automatic producers will call this themselves.
     */
    public void produce() {
        lastProduced = getProductionAmount();
        if (lastProduced > 0) {
            currency.addAmount(lastProduced);
            pcs.firePropertyChange("lastProduced", null, lastProduced);
        }
    }

    /**
     * @return true if the costBase is less than the amount of currency, false if it is not.
     * Fires PropertyChangeEvents for both level and costBase on success.
     */
    public synchronized boolean tryPurchase() {
        if (currency.purchase(this)) {
            doPurchase();
            return true;
        }
        return false;
    }

    private void doPurchase() {
        level++;
        pcs.firePropertyChange("level", null, level);
        pcs.firePropertyChange("cost", null, getCost());
    }

    boolean isAutomatic() {
        return automatic;
    }

    public double getCost() {
        return costBase * Math.pow(costFactor, level);
    }

    /**
     * @return Amount of currency produced by this producer
     */
    double getProductionAmount() {
        return baseProduction * scalingFactor * level;
    }

    double getBaseProduction() {
        return baseProduction;
    }

    double getScalingFactor() {
        return scalingFactor;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(listener);
    }

    public void addPropertyChangeListener(String propertyName, PropertyChangeListener listener) {
        pcs.addPropertyChangeListener(propertyName, listener);
    }
    @Override
    public String toString() {
        return String.format("%s - Level: %d Cost: %e", name, level, getCost());
    }

    @Override
    public void run() {
        while (isRunning()) {
            produce();
            System.out.println(name + " interval = " + interval);
            try {
                Thread.sleep(interval);
            } catch (InterruptedException e) {

            }
        }
    }
}


