package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static model.Game.currency;

/**
 * Produces currency.
 * Use {@link Game#getManualProducer()} and {@link Game#getAutoProducers()} to obtain instances of these for the current game
 */
public class Producer {
    private double baseProduction = 10;
    private double costBase = 5;
    private double costFactor = 1.1;
    private double scalingFactor = 1.0;
    private double lastProduced;
    private int level = 1;
    private boolean automatic = false;
    private String name;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    Producer() {


    }

    Producer(double baseProduction, double costBase, double costFactor, double scalingFactor, int level, boolean automatic, String name) {
        this.baseProduction = baseProduction;
        this.costBase = costBase;
        this.costFactor = costFactor;
        this.scalingFactor = scalingFactor;
        this.level = level;
        this.automatic = automatic;
        this.name = name;
    }

    /**
     * Does production. Fires property change event with amount produced.
     * Automatic producers will call this themselves.
     */
    public void produce() {
        lastProduced = getProductionAmount();
        currency.addAmount(lastProduced);
        pcs.firePropertyChange("lastProduced", null, "+" + lastProduced);

    }

    /**
     * @return true if the cost is less than the amount of currency, false if it is not.
     * Fires PropertyChangeEvents for both level and cost on success.
     */
    public boolean tryPurchase() {
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
        return Math.round(costBase * costFactor * (level + 1));
    }

    /**
     * @return Amount of currency produced by this producer
     */
    double getProductionAmount() {
        return Math.round(baseProduction * scalingFactor * level);
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

    @Override
    public String toString() {
        return String.format("%s - Level: %d Cost: %e", name, level, getCost());
    }
}


