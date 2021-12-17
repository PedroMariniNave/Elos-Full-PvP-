package com.zpedroo.voltzelos.objects;

import com.zpedroo.voltzelos.managers.DataManager;

import java.util.UUID;

public class PlayerData {

    private UUID uuid;
    private Elo elo;
    private int kills;
    private int deaths;
    private long points;
    private boolean update;

    public PlayerData(UUID uuid, Elo elo, int kills, int deaths, long points) {
        this.uuid = uuid;
        this.elo = elo;
        this.kills = kills;
        this.deaths = deaths;
        this.points = points;
        this.update = false;
    }

    public UUID getUUID() {
        return uuid;
    }

    public Elo getElo() {
        return elo;
    }

    public int getKills() {
        return kills;
    }

    public int getDeaths() {
        return deaths;
    }

    public long getPoints() {
        return points;
    }

    public boolean isQueueUpdate() {
        return update;
    }

    public Elo getNextElo() {
        return DataManager.getInstance().getCache().getElos().get(elo.getId() + 1);
    }

    public Elo getPreviousElo() {
        return DataManager.getInstance().getCache().getElos().get(elo.getId() - 1);
    }

    public void addKills(int amount) {
        this.setKills(this.kills + amount);
        this.addPoints(amount * elo.getPointsPerKill());
    }

    public void setKills(int amount) {
        this.kills = amount;
        this.update = true;
    }

    public void addDeaths(int amount) {
        this.setDeaths(this.deaths + amount);
        this.removePoints(amount * elo.getPointsPerDeath());
    }

    public void setDeaths(int amount) {
        this.deaths = amount;
        this.update = true;
    }

    public void addPoints(long amount) {
        this.setPoints(this.points + amount);
    }

    public void removePoints(long amount) {
        this.setPoints(this.points - amount);
    }

    public void setPoints(long amount) {
        this.points = amount > 0 ? amount : 0;
        this.update = true;
    }

    public void setElo(Elo elo) {
        this.elo = elo;
        this.update = true;
    }

    public void setUpdate(boolean update) {
        this.update = update;
    }
}