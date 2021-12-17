package com.zpedroo.voltzelos.hooks;

import com.zpedroo.voltzelos.managers.DataManager;
import com.zpedroo.voltzelos.objects.PlayerData;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

public class PlaceholderAPIHook extends PlaceholderExpansion {

    private Plugin plugin;

    public PlaceholderAPIHook(Plugin plugin) {
        this.plugin = plugin;
    }

    public String getAuthor() {
        return plugin.getDescription().getAuthors().toString();
    }

    public String getIdentifier() {
        return "elos";
    }

    public String getVersion() {
        return plugin.getDescription().getVersion();
    }

    public String onPlaceholderRequest(Player player, String identifier) {
        PlayerData data = DataManager.getInstance().load(player);
        switch (identifier.toUpperCase()) {
            case "ELO":
                return data.getElo() == null ? "-/-" : data.getElo().getTag();
            case "NEXT_ELO":
                return data.getNextElo() == null ? "-/-" : data.getNextElo().getTag();
            default:
                return null;
        }
    }
}