package com.zpedroo.voltzelos.commands;

import com.zpedroo.voltzelos.utils.menus.Menus;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ElosCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) return true;

        Player player = (Player) sender;
        Menus.getInstance().openMainMenu(player);
        return false;
    }
}