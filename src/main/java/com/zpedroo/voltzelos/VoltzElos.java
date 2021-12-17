package com.zpedroo.voltzelos;

import com.zpedroo.voltzelos.commands.ElosCmd;
import com.zpedroo.voltzelos.hooks.PlaceholderAPIHook;
import com.zpedroo.voltzelos.listeners.PlayerGeneralListeners;
import com.zpedroo.voltzelos.managers.DataManager;
import com.zpedroo.voltzelos.managers.EloManager;
import com.zpedroo.voltzelos.mysql.DBConnection;
import com.zpedroo.voltzelos.utils.FileUtils;
import com.zpedroo.voltzelos.utils.menus.Menus;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;
import java.util.logging.Level;

import static com.zpedroo.voltzelos.utils.config.Settings.ELOS_COMMAND;
import static com.zpedroo.voltzelos.utils.config.Settings.ELOS_ALIASES;

public class VoltzElos extends JavaPlugin {

    private static VoltzElos instance;
    public static VoltzElos get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        if (!isMySQLEnabled(getConfig())) {
            getLogger().log(Level.SEVERE, "MySQL are disabled! You need to enable it.");
            getServer().getPluginManager().disablePlugin(this);
            return;
        }

        new DBConnection(getConfig());
        new DataManager();
        new EloManager();
        new PlaceholderAPIHook(this).register();
        new Menus();

        registerCommands();
        registerListeners();
    }

    public void onDisable() {
        if (!isMySQLEnabled(getConfig())) return;

        try {
            DataManager.getInstance().saveAll();
            DBConnection.getInstance().closeConnection();
        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "An error occurred while trying to save data!");
            ex.printStackTrace();
        }
    }

    private void registerCommands() {
        registerCommand(ELOS_COMMAND, ELOS_ALIASES, new ElosCmd());
    }

    private void registerCommand(String command, List<String> aliases, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }

    private boolean isMySQLEnabled(FileConfiguration file) {
        if (!file.contains("MySQL")) return false;

        return file.getBoolean("MySQL.enabled");
    }
}