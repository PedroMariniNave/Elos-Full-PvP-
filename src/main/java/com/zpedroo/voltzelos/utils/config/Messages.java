package com.zpedroo.voltzelos.utils.config;

import com.zpedroo.voltzelos.utils.FileUtils;
import org.bukkit.ChatColor;

import java.util.ArrayList;
import java.util.List;

public class Messages {

    public static final List<String> UPGRADE = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.upgrade"));

    public static final List<String> DOWNGRADE = getColored(FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Messages.downgrade"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    private static List<String> getColored(List<String> list) {
        List<String> colored = new ArrayList<>(list.size());
        for (String str : list) {
            colored.add(getColored(str));
        }

        return colored;
    }
}