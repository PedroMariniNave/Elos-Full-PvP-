package com.zpedroo.voltzelos.utils.config;

import com.zpedroo.voltzelos.utils.FileUtils;

import java.util.List;

public class Settings {

    public static final String ELOS_COMMAND = FileUtils.get().getString(FileUtils.Files.CONFIG, "Settings.commands.elos.command");

    public static final List<String> ELOS_ALIASES = FileUtils.get().getStringList(FileUtils.Files.CONFIG, "Settings.commands.elos.aliases");
}