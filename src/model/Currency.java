package model;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.io.Serializable;

public class Currency implements Serializable {
    private String name = "Objects";
    private double amount;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);


    public Currency(){
        amount = 0;
    }
    public Currency(double amt){
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
