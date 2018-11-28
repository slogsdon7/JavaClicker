package model;


import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/**
 * Use {@link Game#getManualProducer()} and {@link Game#getProducers()} to obtain instances of these for the current game
 */
public class Producer {
    Currency currency;
    private double base_production = 1;
    private double costBase = 5;
    private double costFactor = 1.1;
    private double scaling_factor = 1.0;
    private int count = 1;
    private boolean automatic = false;
    private String name;
    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

     Producer(){

    }

    /**
     * Does production. Fires property change event with amount produced.
     * ActionListeners should call this for manual producers. Automatic producers will call this themselves.
     */
    public void produce(){
        pcs.firePropertyChange("produced", null, getProductionAmount());
    }

    public boolean tryPurchase() {
        return currency.purchase(this);
    }

    public boolean isAutomatic(){ return automatic; }

    public double getCost(){
        return costBase * costFactor * count;
    }

    /**
     * @return Amount of currency produced by this producer
     */
    public double getProductionAmount(){
        return base_production * scaling_factor * count;
    }

    double getBase_production() {
        return base_production;
    }

    double getScaling_factor() {
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
