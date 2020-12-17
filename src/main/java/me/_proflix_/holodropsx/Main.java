package me._proflix_.holodropsx;

import me._proflix_.holodropsx.commands.Check;
import me._proflix_.holodropsx.commands.Reload;
import me._proflix_.holodropsx.listeners.ItemDropListener;
import me._proflix_.holodropsx.listeners.ItemFrameClickListener;
import me._proflix_.holodropsx.listeners.ItemMergeListener;
import me._proflix_.holodropsx.listeners.ItemPickupListener;
import me._proflix_.holodropsx.listeners.protection.BlockDropListener;
import me._proflix_.holodropsx.util.Glow;
import me._proflix_.holodropsx.util.Settings;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    
    public static Main m;
    public Settings settings;
    
    @Override
    public void onEnable() {
        saveDefaultConfig();
        m = this;
        settings = new Settings();
        settings.initialize();
        getServer().getPluginManager().registerEvents(new ItemDropListener(), this);
        getServer().getPluginManager().registerEvents(new ItemMergeListener(), this);
        getServer().getPluginManager().registerEvents(new ItemFrameClickListener(), this);
        getServer().getPluginManager().registerEvents(new ItemPickupListener(), this);
        getServer().getPluginManager().registerEvents(new BlockDropListener(), this);
        getCommand("hdxreload").setExecutor(new Reload());
        getCommand("hdxcheck").setExecutor(new Check());
    
    }
    
    public void onDisable() {
        try {
            Glow.unregister();
        } catch (NoClassDefFoundError error) {
            // this try/catch block is to prevent console spam
        }
        settings.fixNames();
    }
}
