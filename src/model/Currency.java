package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

/**
 * Use {@link Game#getCurrency()} to obtain an instance of this Class.
 * Fires PropertyChangeEvent whenever the currency amount changes.
 */
public class Currency implements Serializable {
    private String name = "Objects";
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

    public synchronized void addAmount(double amount) {
        this.amount += amount;
        pcs.firePropertyChange("Currency", null, amount);
    }


    public double getAmount() {
        return amount;
    }


    public String getName(){
        return name;
    }

    @Override
    public String toString() {
        return Double.toString(amount) + " Objects";
    }
}
