package com.zpedroo.voltzelos.managers;

import com.zpedroo.voltzelos.objects.Elo;
import com.zpedroo.voltzelos.objects.PlayerData;
import com.zpedroo.voltzelos.utils.FileUtils;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.entity.Player;

import java.util.List;

import static com.zpedroo.voltzelos.utils.config.Titles.*;

public class EloManager extends DataManager {

    private static EloManager instance;
    public static EloManager getInstance() { return instance; }

    public EloManager() {
        instance = this;
        this.loadElos();
    }

    public void upgradeElo(Player player) {
        PlayerData data = load(player);
        if (data == null) return;

        Elo elo = data.getElo();
        if (elo == null) return;

        Elo nextElo = data.getNextElo();
        if (nextElo == null) return;

        data.setElo(nextElo);
        for (String cmd : nextElo.getUpgradeCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(cmd, new String[]{
                    "{player}",
                    "{old_elo}",
                    "{new_elo}"
            }, new String[]{
                    player.getName(),
                    elo.getName(),
                    nextElo.getName()
            }));
        }

        player.sendTitle(UPGRADE_TITLE, UPGRADE_SUBTITLE);
        player.playSound(player.getLocation(), Sound.LEVEL_UP, 1f, 1f);
    }

    public void downgradeElo(Player player) {
        PlayerData data = load(player);
        if (data == null) return;

        Elo elo = data.getElo();
        if (elo == null) return;

        Elo previousElo = data.getPreviousElo();
        if (previousElo == null) return;

        data.setElo(previousElo);
        for (String cmd : previousElo.getDowngradeCommands()) {
            Bukkit.dispatchCommand(Bukkit.getConsoleSender(), StringUtils.replaceEach(cmd, new String[]{
                    "{player}",
                    "{old_elo}",
                    "{new_elo}"
            }, new String[]{
                    player.getName(),
                    elo.getName(),
                    previousElo.getName()
            }));
        }

        player.sendTitle(DOWNGRADE_TITLE, DOWNGRADE_SUBTITLE);
        player.playSound(player.getLocation(), Sound.EXPLODE, 0.2f, 10f);
    }

    public boolean canUpgradeElo(Player player) {
        PlayerData data = load(player);
        if (data == null) return false;

        Elo nextElo = data.getNextElo();
        if (nextElo == null) return false;

        return data.getPoints() >= nextElo.getRequiredPoints();
    }

    public boolean canDowngradeElo(Player player) {
        PlayerData data = load(player);
        if (data == null) return false;

        Elo previousElo = data.getPreviousElo();
        if (previousElo == null) return false;

        Elo elo = data.getElo();
        if (elo == null) return false;

        return data.getPoints() < elo.getRequiredPoints();
    }

    private void loadElos() {
        FileUtils.Files file = FileUtils.Files.CONFIG;

        int id = 0;
        for (String eloName : FileUtils.get().getSection(file, "Elos")) {
            if (eloName == null) continue;

            String tag = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Elos." + eloName + ".tag"));
            long pointsPerKill = FileUtils.get().getLong(file, "Elos." + eloName + ".points-per-kill", 1);
            long pointsPerDeath = FileUtils.get().getLong(file, "Elos." + eloName + ".points-per-death", 1);
            int requiredPoints = FileUtils.get().getInt(file, "Elos." + eloName + ".required-points");
            List<String> upgradeCommands = FileUtils.get().getStringList(file, "Elos." + eloName + ".upgrade-commands");
            List<String> downgradeCommands = FileUtils.get().getStringList(file, "Elos." + eloName + ".downgrade-commands");

            getCache().getElos().put(++id, new Elo(eloName, tag, upgradeCommands, downgradeCommands, pointsPerKill, pointsPerDeath, requiredPoints, id));
        }
    }

    public Elo getElo(String eloName) {
        for (Elo elo : getCache().getElos().values()) {
            if (!elo.getName().equals(eloName)) continue;

            return elo;
        }

        return null;
    }

    public Elo getDefaultElo() {
        return getElo(1);
    }

    public Elo getElo(int id) {
        return getCache().getElos().get(id);
    }
}