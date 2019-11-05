package com.stirante.firent.entities;

import org.bukkit.Location;

public abstract class MagicEntity {

    private Location location;
    private Location centerLocation;
    private boolean alive = true;

    public MagicEntity(Location location) {
        setLocation(location);
    }

    public void update() {
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
        centerLocation = location.clone().add(0.5, 1, 0.5);
    }

    public void kill() {
        alive = false;
    }

    public boolean isAlive() {
        return alive;
    }

    public Location getCenterLocation() {
        return centerLocation;
    }
}
