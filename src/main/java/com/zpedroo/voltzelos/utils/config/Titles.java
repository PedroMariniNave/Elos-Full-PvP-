package com.zpedroo.voltzelos.utils.config;

import com.zpedroo.voltzelos.utils.FileUtils;
import org.bukkit.ChatColor;

public class Titles {

    public static final String UPGRADE_TITLE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.upgrade.title"));

    public static final String UPGRADE_SUBTITLE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.upgrade.subtitle"));

    public static final String DOWNGRADE_TITLE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.downgrade.title"));

    public static final String DOWNGRADE_SUBTITLE = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Titles.downgrade.subtitle"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}