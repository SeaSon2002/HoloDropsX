package me._proflix_.holodropsx.util;

import me._proflix_.holodropsx.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

/**
 * The type Strings.
 */
public class Strings {

    private final static Function<String, String> FUNCTION;

    static {
        //If server version is 1.16 or higher, make the function translte from bungeecord-chat API
        if (Bukkit.getBukkitVersion().contains("1.16")) {
            FUNCTION = s -> net.md_5.bungee.api.ChatColor.translateAlternateColorCodes('&', s);
            //Else make it compatible with the bukkit class
        } else {
            FUNCTION = s -> org.bukkit.ChatColor.translateAlternateColorCodes('&', s);
        }
    }

    private void Utils() {
        throw new ExceptionInInitializerError("This class may not be initialized"); //Let no one invoke this class
    }

    /**
     * Color string.
     *
     * @param string the string
     * @return the string
     */
    public static String color(String string) {
        return FUNCTION.apply(string);
    }

    /**
     * Color list list.
     *
     * @param list the list
     * @return the list
     */
    public static List<String> colorList(List<String> list) { // color the glowlist
        for (int x = 0; x < list.size(); x++) {
            list.set(x, color(list.get(x)));
        }
        return list;
    }

    /**
     * Strip color string.
     *
     * @param string the string
     * @return the string
     */
    public static String stripColor(String string) {
        return ChatColor.stripColor(string);
    }

    /**
     * Make name string.
     *
     * @param drop       the drop
     * @param count      the count
     * @param playerName the player name
     * @param time       the time
     * @return the string
     */
    public static String makeName(Item drop, int count, String playerName, int time) {
        String formatted = !playerName.equals("") ? Main.m.settings.getProtFormat() + Main.m.settings.getFormat() : Main.m.settings.getFormat();
        String itemName;
        itemName = makeItemName(drop);
        if (Main.m.settings.isBlacklisted(itemName) || isUUID(itemName)) {
            itemName = "";
        }
        formatted = rePlaceholders(formatted, itemName, count, playerName, time);
        
        return itemName.length() == 0 ? itemName : formatted;
        
    }

    /**
     * Make item name string.
     *
     * @param drop the drop
     * @return the string
     */
    public static String makeItemName(Item drop) {
        String itemName;
        
        ItemMeta meta = drop.getItemStack().getItemMeta();
        
        if (drop.getItemStack().getType() == Material.WRITTEN_BOOK) {
            assert meta != null;
            itemName = bookTitle((BookMeta)meta);
        }
        else {
            assert meta != null;
            if (meta.hasDisplayName() || Main.m.settings.getCustomNamesOnly()) {
                itemName = meta.getDisplayName();
            } else {
                itemName = Main.m.settings.getNameFromMat(drop.getItemStack().getType().toString());
            }
        }
        
        return itemName;
    }
    
    private static String bookTitle(BookMeta meta) {
        String title = meta.getTitle() == null ? " " : meta.getTitle();
        String itemName = me._proflix_.holodropsx.util.ConfigReader.getString("item-names.WRITTEN_BOOK");
        return itemName.replace("%title%", title);
    }
    
    private static String rePlaceholders(String formatted, String item, int count, String playerName, int time) {
        formatted = replaceAndFixSpacing(formatted, "%P%", Main.m.settings.getPrefix());
        formatted = replaceAndFixSpacing(formatted, "%I%", item);
        formatted = replaceAndFixSpacing(formatted, "%S%", Main.m.settings.getSuffix());
        formatted = replaceAndFixSpacing(formatted, "%PLAYER%", playerName);
        formatted = replaceAndFixSpacing(formatted, "%TIME%", time != 0 ? time + "" : "");
        // single stacks
        // count != 0 is for item frames (never display the stack count)
        if (count != 0 && count != 1 || Main.m.settings.getSingleStack()) {
            formatted = formatted.replace("%C%", Main.m.settings.getStackFormat().toLowerCase().replace("%amount%", "" + count));
        } else {
            formatted = replaceAndFixSpacing(formatted, "%C%", "");
            
        }
        return formatted;
    }
    
    private static String replaceAndFixSpacing(String string, String replace, String replacement) {
        // remove spaces that would have made sense if the placeholder was there
        // %PLAYER% %ITEM%, no player would make it " %ITEM%" or " DIRT"
        //                                           ^
        if (replacement.equals("")) {
            return string.replace(" " + replace + " ", "")
                    .replace(" " + replace, "")
                    .replace(replace + " ", "")
                    .replace(replace, "");
        }
        return string.replace(replace, replacement);
    }

    /**
     * Make item frame name.
     *
     * @param item  the item
     * @param count the count
     */
// count is supplied to make a call to rePlaceholders
    public static void makeItemFrameName(ItemStack item, int count) {
        ItemMeta meta = item.getItemMeta();
        String formatted = Main.m.settings.getFormat().toUpperCase();
        String itemName;
    
        if (item.getType() == Material.WRITTEN_BOOK) {
            assert meta != null;
            itemName = bookTitle((BookMeta)meta);
        } else {
            itemName = Main.m.settings.getNameFromMat(item.getType().name());
        }
        formatted = rePlaceholders(formatted, itemName, count, "", 0);

        assert meta != null;
        meta.setDisplayName(itemName.length() == 0 ? itemName : formatted);
        item.setItemMeta(meta);
        
    }

    /**
     * Add watermark.
     *
     * @param item the item
     */
    public static void addWatermark(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        ArrayList<String> lore;
        if (meta.hasLore()) lore = (ArrayList<String>) meta.getLore();
        else lore = new ArrayList<>();
        assert lore != null;
        lore.add("HoloDrops");
        meta.setLore(lore);
        item.setItemMeta(meta);
    }

    /**
     * Remove watermark.
     *
     * @param item the item
     */
    public static void removeWatermark(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        if (hasWatermark(item)) {
            assert meta != null;
            List<String> lore = meta.getLore();
            assert lore != null;
            lore.remove(lore.size() - 1);
            meta.setLore(lore);
            meta.setDisplayName("");
            item.setItemMeta(meta);
        }
    }

    /**
     * Has watermark boolean.
     *
     * @param item the item
     * @return the boolean
     */
    public static boolean hasWatermark(ItemStack item) {
        ItemMeta meta = item.getItemMeta();
        assert meta != null;
        if (meta.hasLore()) {
            List<String> lore = meta.getLore();
            // last line has watermark
            assert lore != null;
            return lore.get(lore.size() - 1).equals("HoloDrops");
        }
        return false;
    }

    /**
     * Is uuid boolean.
     *
     * @param name the name
     * @return the boolean
     */
    public static boolean isUUID(String name) {
        return (stripColor(name).matches("[0-9a-f]{8}-[0-9a-f]{4}-[1-5][0-9a-f]{3}-[89ab][0-9a-f]{3}-[0-9a-f]{12}"));
    }
    
    
}
