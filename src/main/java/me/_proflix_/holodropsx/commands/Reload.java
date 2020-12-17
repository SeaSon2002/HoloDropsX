package me._proflix_.holodropsx.commands;

import me._proflix_.holodropsx.Main;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class Reload implements CommandExecutor {
    
    private String prefix = "" + ChatColor.DARK_RED + ChatColor.BOLD + "HoloDropsX ";
    
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        reload(sender);
        return true;
    }
    
    private void reload(CommandSender sender) {
        Main.m.settings.initialize();
        sender.sendMessage(prefix + ChatColor.RESET + ChatColor.GREEN + "Successfully reloaded the config");
    }
    
}
