package com.zpedroo.voltzelos.listeners;

import br.com.devpaulo.legendchat.api.events.ChatMessageEvent;
import com.zpedroo.voltzelos.managers.DataManager;
import com.zpedroo.voltzelos.managers.EloManager;
import com.zpedroo.voltzelos.objects.PlayerData;
import com.zpedroo.voltzelos.utils.config.Messages;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onQuit(PlayerQuitEvent event) {
        DataManager.getInstance().save(event.getPlayer());
    }

    @EventHandler(priority = EventPriority.MONITOR)
    public void onDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player == null) return;

        Player killer = player.getKiller();
        if (killer == null) return;

        PlayerData playerData = DataManager.getInstance().load(player);
        if (playerData == null) return;

        playerData.addDeaths(1);

        if (EloManager.getInstance().canDowngradeElo(player)) {
            EloManager.getInstance().downgradeElo(player);

            for (String msg : Messages.UPGRADE) {
                Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                        "{player}",
                        "{elo_tag}"
                }, new String[]{
                        killer.getName(),
                        playerData.getElo().getTag()
                }));
            }
        }

        PlayerData killerData = DataManager.getInstance().load(killer);
        if (killerData == null) return;

        killerData.addKills(1);

        if (!EloManager.getInstance().canUpgradeElo(killer)) return;

        EloManager.getInstance().upgradeElo(killer);
        killer.getWorld().spigot().strikeLightning(killer.getLocation(), true);

        for (String msg : Messages.UPGRADE) {
            Bukkit.broadcastMessage(StringUtils.replaceEach(msg, new String[]{
                    "{player}",
                    "{elo_tag}"
            }, new String[]{
                    killer.getName(),
                    killerData.getElo().getTag()
            }));
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onChat(ChatMessageEvent event) {
        if (!event.getTags().contains("elo")) return;

        PlayerData data = DataManager.getInstance().load(event.getSender());
        if (data.getElo() == null) return;

        event.setTagValue("elo", data.getElo().getTag());
    }
}