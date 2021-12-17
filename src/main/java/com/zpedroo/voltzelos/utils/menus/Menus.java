package com.zpedroo.voltzelos.utils.menus;

import com.zpedroo.voltzelos.managers.DataManager;
import com.zpedroo.voltzelos.managers.EloManager;
import com.zpedroo.voltzelos.objects.Elo;
import com.zpedroo.voltzelos.objects.PlayerData;
import com.zpedroo.voltzelos.utils.FileUtils;
import com.zpedroo.voltzelos.utils.builder.InventoryBuilder;
import com.zpedroo.voltzelos.utils.builder.InventoryUtils;
import com.zpedroo.voltzelos.utils.builder.ItemBuilder;
import com.zpedroo.voltzelos.utils.formatter.NumberFormatter;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class Menus extends InventoryUtils {

    private static Menus instance;
    public static Menus getInstance() { return instance; }

    private ItemStack nextPageItem;
    private ItemStack previousPageItem;

    public Menus() {
        instance = this;
        this.nextPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Next-Page").build();
        this.previousPageItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Previous-Page").build();
    }

    public void openMainMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.MAIN;

        String title = getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);
        PlayerData data = DataManager.getInstance().load(player);

        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str, new String[]{
                    "{player}",
                    "{kills}",
                    "{deaths}",
                    "{points}",
                    "{required_points}"
            }, new String[]{
                    player.getName(),
                    NumberFormatter.formatDecimal(data.getKills()),
                    NumberFormatter.formatDecimal(data.getDeaths()),
                    NumberFormatter.formatDecimal(data.getPoints()),
                    data.getNextElo() == null ? "-/-" : NumberFormatter.formatDecimal(data.getNextElo().getRequiredPoints()),
            }).build();
            int slot = FileUtils.get().getInt(file, "Inventory.items." + str + ".slot");
            String action = FileUtils.get().getString(file, "Inventory.items." + str + ".action");

            inventory.addItem(item, slot, () -> {
                switch (action.toUpperCase()) {
                    case "ELOS":
                        openElosMenu(player);
                        break;
                    case "TOP":
                        openTopMenu(player);
                        break;
                }
            }, ActionType.ALL_CLICKS);
        }

        inventory.open(player);
    }

    public void openElosMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.ELOS;

        String title = getColored(FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        int nextPageSlot = FileUtils.get().getInt(file, "Inventory.next-page-slot");
        int previousPageSlot = FileUtils.get().getInt(file, "Inventory.previous-page-slot");

        InventoryBuilder inventory = new InventoryBuilder(title, size, previousPageItem, previousPageSlot, nextPageItem, nextPageSlot);

        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");
        int i = -1;

        PlayerData data = DataManager.getInstance().load(player);
        for (String str : FileUtils.get().getSection(file, "Inventory.items")) {
            if (++i >= slots.length) i = 0;

            Elo elo = EloManager.getInstance().getElo(str);
            if (elo == null) continue;

            String status = data.getElo().getId() >= elo.getId() ? "unlocked" : "locked";

            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(file).get(), "Inventory.items." + str + "." + status, new String[]{
                    "{required_points}",
                    "{status}"
            }, new String[]{
                    NumberFormatter.formatDecimal(elo.getRequiredPoints()),
                    getColored(FileUtils.get().getString(file, "Lore-Filter." + status))
            }).build();
            int slot = Integer.parseInt(slots[i]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }

    public void openTopMenu(Player player) {
        FileUtils.Files file = FileUtils.Files.TOP;

        String title = ChatColor.translateAlternateColorCodes('&', FileUtils.get().getString(file, "Inventory.title"));
        int size = FileUtils.get().getInt(file, "Inventory.size");

        InventoryBuilder inventory = new InventoryBuilder(title, size);

        int pos = 0;
        String[] slots = FileUtils.get().getString(file, "Inventory.slots").replace(" ", "").split(",");

        for (PlayerData data : DataManager.getInstance().getCache().getTopPoints()) {
            ItemStack item = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.TOP).get(), "Item", new String[]{
                    "{player}",
                    "{kills}",
                    "{deaths}",
                    "{points}",
                    "{pos}"
            }, new String[]{
                    NumberFormatter.formatDecimal(data.getKills()),
                    NumberFormatter.formatDecimal(data.getDeaths()),
                    NumberFormatter.formatDecimal(data.getPoints()),
                    String.valueOf(++pos)
            }).build();

            int slot = Integer.parseInt(slots[pos-1]);

            inventory.addItem(item, slot);
        }

        inventory.open(player);
    }

    private String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}