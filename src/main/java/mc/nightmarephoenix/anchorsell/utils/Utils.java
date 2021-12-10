package mc.nightmarephoenix.anchorsell.utils;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.hooks.FactionsX;
import mc.nightmarephoenix.anchorsell.hooks.Hooks;
import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
//import net.prosavage.factionsx.core.FPlayer;
//import net.prosavage.factionsx.manager.GridManager;
//import net.prosavage.factionsx.manager.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ExecutionException;

public class Utils {

    /**
     * Translates the color codes.
     * @param str
     * @return
     */
    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    /**
     * Same as above but with a list.
     * @param strList
     * @return
     */
    public static List<String> Color(List<String> strList) {
        for(String string: strList) {
            strList.set(strList.indexOf(string), Color(string));
        }
        return strList;
    }

    /**
     * Returns a new anchor with a given level and the quantity.
     * @param level
     * @param quantity
     * @return ItemStack
     */
    public static ItemStack getAnchor(int level, int quantity) {
        ItemStack item = new ItemStack(Material.RESPAWN_ANCHOR, quantity);
        ArrayList<String> Lore = new ArrayList<String>();
        Lore.add("");
        Lore.add(Utils.Color("&7&m----------------------------"));
        Lore.add(Utils.Color("&fAnchor level: &e" + level));
        Lore.add(Utils.Color("&fMoney per minute: &e" + Utils.getMoneyPerMinute(level)));
        Lore.add(Utils.Color("&7&m----------------------------"));
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(Utils.Color("&5&lAnchor"));
        meta.setLore(Lore);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns the anchor level but with the ore.
     * @param plugin
     * @param level
     * @return
     */
    public static String getAnchorOreLevelString(AnchorSell plugin, int level) {
        if(level > 64)
            return Color(plugin.getConfig().getString("levels.maxed-out-level"));
        else if(level < 16)
            return Color(plugin.getConfig().getString("levels.1"));
        else if(level < 24)
            return Color(plugin.getConfig().getString("levels.2"));
        else if(level < 32)
            return Color(plugin.getConfig().getString("levels.3"));
        else if(level < 48)
            return Color(plugin.getConfig().getString("levels.4"));
        else if(level <= 64)
            return Color(plugin.getConfig().getString("levels.5"));
        else
            return null;
    }

    /**
     * Anchor level translated to ores.
     * @param level
     * @return
     */
    public static Material getAnchorOreLevel(int level) {
        if(level < 16)
            return Material.COAL;
        else if(level < 24)
            return Material.IRON_INGOT;
        else if(level < 32)
            return Material.GOLD_INGOT;
        else if(level < 48)
            return Material.DIAMOND;
        else if(level <= 64)
            return Material.NETHERITE_INGOT;
        else
            return null;
    }

    /**
     * Money per second with a given anchor level.
     * @param anchorLevel
     * @return double
     */
    public static double getMoneyPerSecond(int anchorLevel) {
        return 0.1 * anchorLevel + Math.pow(anchorLevel, 0.8);
    }

    /**
     * Money per minute with a given anchor level.
     * @param anchorLevel
     * @return double
     */
    public static double getMoneyPerMinute(int anchorLevel) {
        return Math.round(getMoneyPerSecond(anchorLevel) * 60);
    }

    /**
     * Money to the next upgrade.
     * @param anchorLevel
     * @param plugin
     * @return double
     */
    public static double getMoneyToUpgrade(int anchorLevel, AnchorSell plugin) {
        return getMoneyPerMinute(anchorLevel + 1) * 60 * plugin.getConfig().getInt("anchor.upgrade-multiplier"); // the money that gives the next level multiplied by 16hs
    }

    /**
     * Gets the lore of a given anchor.
     * @param path
     * @param plugin
     * @param location
     * @param player
     * @return
     */
    public static  List<String> getLore(String path, AnchorSell plugin, Location location, Player player) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(plugin, location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level, plugin));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }

            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(plugin, level)).
                    replaceAll("%playerBalance%", String.valueOf(EconomyManager.getEconomy().getBalance(player))).
                    replaceAll("%playerAnchors%", String.valueOf(StorageManager.getPlayerTotalAnchors(plugin, player))).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%playerMoneyPer15Minutes%", String.valueOf(StorageManager.getPlayerMoneyPerMinute(plugin, player) * 15)).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(plugin, level + 1)));
        }
        return res;
    }

    /**
     * Gets the lore of an anchor but without the player.
     * @param path
     * @param plugin
     * @param location
     * @return
     */
    public static  List<String> getLore(String path, AnchorSell plugin, Location location) {
        List<String> res = new ArrayList<>();
        for(String str: Utils.Color(plugin.getConfig().getStringList(path))) {
            int level = StorageManager.getAnchorLevel(plugin, location);
            String levelToUpgrade = String.valueOf(level + 1);
            String priceOfUpgrade = String.valueOf(Utils.getMoneyToUpgrade(level, plugin));
            if((level + 1) > 64) {
                levelToUpgrade = "";
                priceOfUpgrade = "-";
            }
            res.add(str.replaceAll("%level%", String.valueOf(level)).
                    replaceAll("%moneyPer15Minutes%", String.valueOf(Utils.getMoneyPerMinute(level) * 15)).
                    replaceAll("%moneyPerMinute%", String.valueOf(Utils.getMoneyPerMinute(level))).
                    replaceAll("%oreLevel%", Utils.getAnchorOreLevelString(plugin, level)).
                    replaceAll("%maxPlayerAnchors%", String.valueOf(plugin.getConfig().getInt("total-anchors-user-can-have"))).
                    replaceAll("%priceOfUpgrade%", priceOfUpgrade).
                    replaceAll("%nextLevel%", levelToUpgrade).
                    replaceAll("%nextLevelOre%", Utils.getAnchorOreLevelString(plugin, level + 1)));
        }
        return res;
    }

    /**
     * Creates a determined item.
     * @param name
     * @param material
     * @param enchanted
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Creates a item with lore.
     * @param name
     * @param material
     * @param lore
     * @param enchanted
     * @return ItemStack
     */
    public static ItemStack createItem(String name, Material material, List<String> lore, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(lore);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

    /**
     * Returns if the player is inside his Faction claim.
     * (FactionsX hook)
     * @param block
     * @param p
     * @return boolean
     */
    public static boolean isPlayerInHisFaction(Block block, Player p) {
        if(Hooks.getFactionsX() == FactionsX.ACTIVE) {
            return false; //String.valueOf(GridManager.INSTANCE.getFactionAt(block.getChunk()).getLeader()).equals(String.valueOf(PlayerManager.INSTANCE.getFPlayer(p).getFaction().getLeader()));
        }
        return false;
    }

    /**
     * Returns if a block is in wilderness.
     * (FactionsX hook)
     * @param block
     * @return
     */
    public static boolean isBlockInWilderness(Block block) {
        if(Hooks.getFactionsX() == FactionsX.ACTIVE) {
//            return GridManager.INSTANCE.getFactionAt(block.getChunk()).getLeader() == null;
            return false;
        }
        return false;
    }

    /**
     * Returns if is from a faction member.
     * @param plugin
     * @param breaker
     * @param block
     * @return boolean
     */
    public static boolean isAFactionMember(AnchorSell plugin, Player breaker, Block block) {
//        if(Hooks.getFactionsX() == FactionsX.ACTIVE) {
//            for(FPlayer p: GridManager.INSTANCE.getFactionAt(block.getChunk()).getMembers()) {
//                if(breaker.getName().equals(p.getName())) {
//                    if(!StorageManager.isMyAnchor(block.getLocation(), breaker, plugin)) {
//                        return true;
//                    }
//                }
//            }
//        }
        return false;
    }

    /**
     * Sort hashmap by values (TOP)
     * @param hm
     * @return
     */
    public static HashMap<String, Integer> sortHashMapByValue(HashMap<String, Integer> hm) {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }

    /**
     * Sends console message.
     * @param msg
     */
    public static void sendMessage(String msg) {
        Bukkit.getConsoleSender().sendMessage(Utils.Color("&5&lAnchorSell &f - " + msg));
    }

    public static void sendConfigMessage(String str, CommandSender sender) {
        try {
            sender.sendMessage(Color(Global.plugin.getConfig().getString(str)));
        } catch (Exception e) {
            sendMessage("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    public static void sendConfigMultilineMessage(String message, CommandSender sender) {
        try {
            for(String line: Global.plugin.getConfig().getStringList(message)) {
                sender.sendMessage(Utils.Color(line));
            }
        } catch (Exception e) {
            sendMessage("Check for config updates: &ahttps://github.com/FranciscoDadone/AnchorSell/blob/main/src/main/resources/config.yml");
        }
    }

    public static boolean isNumeric(String strNum) {
        if (strNum == null) {
            return false;
        }
        try {
            double d = Double.parseDouble(strNum);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

}
