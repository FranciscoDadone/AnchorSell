package com.franciscodadone.anchorsell.models;

import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;

public class Anchor {

    public Anchor(int level, Location location, Player owner, String ownerName) {
        this.level     = level;
        this.location  = location;
        this.owner     = owner;
        this.ownerName = ownerName;
    }

    public Anchor(int level, Location location, OfflinePlayer owner, String ownerName) {
        this.level     = level;
        this.location  = location;
        this.owner     = owner;
        this.ownerName = ownerName;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public OfflinePlayer getOwner() {
        return owner;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getOwnerName() {
        return ownerName;
    }

    private Location location;
    private final OfflinePlayer owner;
    private int level;
    private final String ownerName;

}
