package com.zpedroo.voltzelos.managers.cache;

import com.zpedroo.voltzelos.mysql.DBConnection;
import com.zpedroo.voltzelos.objects.PlayerData;
import com.zpedroo.voltzelos.objects.Elo;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataCache {

    private Map<Integer, Elo> elos;
    private Map<Player, PlayerData> playerData;
    private List<PlayerData> topPoints;

    public DataCache() {
        this.elos = new HashMap<>(24);
        this.playerData = new HashMap<>(64);
        this.topPoints = DBConnection.getInstance().getDBManager().getTopPoints();
    }

    public Map<Integer, Elo> getElos() {
        return elos;
    }

    public Map<Player, PlayerData> getPlayerData() {
        return playerData;
    }

    public List<PlayerData> getTopPoints() {
        return topPoints;
    }

    public void setTopPoints(List<PlayerData> topPoints) {
        this.topPoints = topPoints;
    }
}