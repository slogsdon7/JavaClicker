package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import static model.Game.currency;

/**
 * Produces currency.
 * Use {@link Game#getManualProducer()} and {@link Game#getProducers()} to obtain instances of these for the current game
 */
public class Producer {
    private double base_production = 1;
    private double costBase = 5;
    private double costFactor = 1.1;
    private double scaling_factor = 1.0;
    private int level = 1;
    private boolean automatic = false;
    private String name;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

     Producer(){


    }

    /**
     * Does production. Fires property change event with amount produced.
     * Automatic producers will call this themselves.
     */
    public void produce(){
        currency.addAmount(getProductionAmount());
        pcs.firePropertyChange("amountProduced", null,"+" + getProductionAmount());

    }

    /**
     *
     * @return true if the cost is less than the amount of currency, false if it is not.
     * Fires PropertyChangeEvents for both level and cost on success.
     */
    public boolean tryPurchase() {
        if(currency.purchase(this)){
            doPurchase();
            return true;
        }
            return false;
    }

    private void doPurchase(){
        level++;
        pcs.firePropertyChange("level", null, level);
        pcs.firePropertyChange("cost",null, getCost());
    }

    boolean isAutomatic(){ return automatic; }

    public double getCost(){
        return costBase * costFactor * level;
    }

    /**
     * @return Amount of currency produced by this producer
     */
    double getProductionAmount(){
        return base_production * scaling_factor * level;
    }

    double getBase_production() {
        return base_production;
    }

    double getScaling_factor() {
        return scaling_factor;
    }

    public int getLevel() {
        return level;
    }

    public String getName() {
        return name;
    }
    void setName(String name){
        this.name = name;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

}
