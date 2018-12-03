package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Contains logic related to game currency.
 * Use {@link Game#getCurrency()} to obtain an instance of this Class.
 * Fires PropertyChangeEvent whenever the currency amount changes.
 */
public class Currency implements Serializable {
    private String name = "Garbage";
    private double amount;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


   Currency(){
        amount = 0;
    }
    Currency(double amt){
        amount = amt;
    }



    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

    synchronized void addAmount(double amount) {
        this.amount += Math.round(amount);
        pcs.firePropertyChange("amount", null, this.amount);
    }

    synchronized void subtractAmount(double amount) {
        this.amount -= amount;
        pcs.firePropertyChange("amount", null, this.amount);
    }

    public double getAmount() {
        return amount;
    }


    public String getName(){
        return name;
    }

    synchronized boolean purchase(Producer producer){
        double cost = producer.getCost();
       if (canAfford(cost)){
           subtractAmount(cost);
           return true;
       }
       return false;
    }

    synchronized boolean purchase(Upgrade upgrade){
       return false;
    }

    boolean canAfford(double cost){
       return amount > cost;
    }

    @Override
    public String toString() {
        return String.format("%.2f Instances", amount);
    }
}
