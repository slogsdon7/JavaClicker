package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Producer {
    private double base_production = 1;
    private double scaling_factor = 1.0;
    private int count = 1;
    private boolean automatic = false;
    private String name;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public Producer(){

    }

    public void produce(){
        double produced = base_production * scaling_factor * count;
        pcs.firePropertyChange("produced", null, produced);
    }

    public boolean isAutomatic(){
        return automatic;
    }

    public double getBase_production() {
        return base_production;
    }

    public double getScaling_factor() {
        return scaling_factor;
    }

    public int getCount() {
        return count;
    }

    public String getName() {
        return name;
    }
    protected void setName(String name){
        this.name = name;
    }

    public void addPropertyChangeListener(PropertyChangeListener listener){
        pcs.addPropertyChangeListener(listener);
    }

}
