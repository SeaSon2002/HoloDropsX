package me._proflix_.holodropsx.listeners;

import me._proflix_.holodropsx.Main;
import me._proflix_.holodropsx.util.Strings;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Rotation;
import org.bukkit.entity.ItemFrame;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

import java.util.Objects;

public class ItemFrameClickListener implements Listener {
    
    @EventHandler
    public void clickFrame(PlayerInteractEntityEvent e)
    {
        Player p = e.getPlayer();
        if (e.getRightClicked() instanceof ItemFrame && Main.m.settings.getFrames() && e.getHand() == EquipmentSlot.HAND) {
            ItemFrame frame = (ItemFrame)e.getRightClicked();
            if (Main.m.settings.isWorldEnabled(frame.getWorld().getName())) {
                // empty frame
                if (frame.getItem().getType() == Material.AIR && p.getItemOnCursor().getType() != Material.AIR) {
                    // make a new ItemStack to not change the one in hand
                    ItemStack newone = p.getItemOnCursor().clone();
                    // if it doesnt have a name AND its not on custom names only mode: put a name on it to display
                    // custom named items will always display (vanilla feature)
                    if (!Objects.requireNonNull(newone.getItemMeta()).hasDisplayName() && !Main.m.settings.getCustomNamesOnly()) {
                        Strings.makeItemFrameName(newone, 0);
                        Strings.addWatermark(newone);
                    }
                    frame.setItem(newone);
                    frame.setRotation(Rotation.COUNTER_CLOCKWISE_45);
                    if (p.getGameMode() != GameMode.CREATIVE) {
                        p.getItemOnCursor().setAmount(p.getItemOnCursor().getAmount() - 1);
                    }
                }
            }
        }
    }
    
}
