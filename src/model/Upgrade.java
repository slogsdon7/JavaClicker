package model;

import java.io.Serializable;

public class Upgrade implements Serializable {
    private double cost;
    private String description;
    private boolean purchased = false;
}
