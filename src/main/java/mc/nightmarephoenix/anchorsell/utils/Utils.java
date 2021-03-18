package mc.nightmarephoenix.anchorsell.utils;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.economy.EconomyManager;
import mc.nightmarephoenix.anchorsell.hooks.FactionsX;
import mc.nightmarephoenix.anchorsell.hooks.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import net.prosavage.factionsx.core.FPlayer;
import net.prosavage.factionsx.manager.GridManager;
import net.prosavage.factionsx.manager.PlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class Utils {

    public static String Color(String str) {
        return ChatColor.translateAlternateColorCodes('§', str.replace("&", "§"));
    }

    public static List<String> Color(List<String> strList) {
        for(String string: strList) {
            strList.set(strList.indexOf(string), Color(string));
        }
        return strList;
    }

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

    public static double getMoneyPerSecond(int anchorLevel) {
        return 0.1 * anchorLevel + Math.pow(anchorLevel, 0.8);
    }

    public static double getMoneyPerMinute(int anchorLevel) {
        return Math.round(getMoneyPerSecond(anchorLevel) * 60);
    }

    public static double getMoneyToUpgrade(int anchorLevel, AnchorSell plugin) {
        return getMoneyPerMinute(anchorLevel + 1) * 60 * plugin.getConfig().getInt("anchor.upgrade-multiplier"); // the money that gives the next level multiplied by 16hs
    }

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

    public static ItemStack createItem(String name, Material material, boolean enchanted) {
        ItemStack item = new ItemStack(material, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName(name);
        if(enchanted)
            meta.addEnchant(Enchantment.DURABILITY, 1, true);
        item.setItemMeta(meta);
        return item;
    }

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

    public static boolean isPlayerInHisFaction(Block block, Player p) {
        if(Global.getFactionsX() == FactionsX.ACTIVE) {
            if(String.valueOf(GridManager.INSTANCE.getFactionAt(block.getChunk()).getLeader()).equals(String.valueOf(PlayerManager.INSTANCE.getFPlayer(p).getFaction().getLeader()))) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isBlockInWilderness(Block block) {
        if(Global.getFactionsX() == FactionsX.ACTIVE) {
            if(GridManager.INSTANCE.getFactionAt(block.getChunk()).getLeader() == null) {
                return true;
            }
            return false;
        }
        return false;
    }

    public static boolean isAFactionMember(AnchorSell plugin, Player breaker, Block block) {
        if(Global.getFactionsX() == FactionsX.ACTIVE) {
            for(FPlayer p: GridManager.INSTANCE.getFactionAt(block.getChunk()).getMembers()) {
                if(breaker.getName().equals(p.getName())) {
                    if(!StorageManager.isMyAnchor(block.getLocation(), breaker, plugin)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    // function to sort hashmap by values
    public static HashMap<String, Integer> sortHashMapByValue(HashMap<String, Integer> hm)
    {
        // Create a list from elements of HashMap
        List<Map.Entry<String, Integer> > list =
                new LinkedList<Map.Entry<String, Integer> >(hm.entrySet());

        // Sort the list
        Collections.sort(list, new Comparator<Map.Entry<String, Integer> >() {
            public int compare(Map.Entry<String, Integer> o1,
                               Map.Entry<String, Integer> o2)
            {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        // put data from sorted list to hashmap
        HashMap<String, Integer> temp = new LinkedHashMap<String, Integer>();
        for (Map.Entry<String, Integer> aa : list) {
            temp.put(aa.getKey(), aa.getValue());
        }
        return temp;
    }
}
