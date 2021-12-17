package com.zpedroo.voltzelos.objects;

import java.util.List;

public class Elo {

    private String name;
    private String tag;
    private List<String> upgradeCommands;
    private List<String> downgradeCommands;
    private long pointsPerKill;
    private long pointsPerDeath;
    private int requiredPoints;
    private int id;

    public Elo(String name, String tag, List<String> upgradeCommands, List<String> downgradeCommands, long pointsPerKill, long pointsPerDeath, int requiredPoints, int id) {
        this.name = name;
        this.tag = tag;
        this.upgradeCommands = upgradeCommands;
        this.downgradeCommands = downgradeCommands;
        this.pointsPerKill = pointsPerKill;
        this.pointsPerDeath = pointsPerDeath;
        this.requiredPoints = requiredPoints;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getTag() {
        return tag;
    }

    public long getPointsPerKill() {
        return pointsPerKill;
    }

    public long getPointsPerDeath() {
        return pointsPerDeath;
    }

    public int getRequiredPoints() {
        return requiredPoints;
    }

    public List<String> getUpgradeCommands() {
        return upgradeCommands;
    }

    public List<String> getDowngradeCommands() {
        return downgradeCommands;
    }

    public int getId() {
        return id;
    }
}