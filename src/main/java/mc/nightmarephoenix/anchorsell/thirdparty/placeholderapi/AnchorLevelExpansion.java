package mc.nightmarephoenix.anchorsell.thirdparty.placeholderapi;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.models.Anchor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class AnchorLevelExpansion extends PlaceholderExpansion {

    public AnchorLevelExpansion(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @Override
    public @NotNull String getIdentifier() {
        return "AnchorSell";
    }

    @Override
    public @NotNull String getAuthor() {
        return "Francisco Dadone";
    }

    @Override
    public @NotNull String getVersion() {
        return "1.0.1";
    }

    @Override
    public boolean persist() {
        return false; // This is required or else PlaceholderAPI will unregister the Expansion on reload
    }

    @Override
    public String onRequest(OfflinePlayer player, String params) {

        // Player level (sum of all levels of all anchors from that player)
        if(params.equalsIgnoreCase("playerlevel")) {
            int level = 0;
            for(Anchor anchor : StorageManager.getAllPlayerAnchors(player)) {
                level += anchor.getLevel();
            }
            return String.valueOf(level);
        }

        // Total player anchors
        if(params.equalsIgnoreCase("playeranchors")) {
            return String.valueOf(StorageManager.getPlayerTotalAnchors(player));
        }

        // Money per minute that the player generates via anchors.
        if(params.equalsIgnoreCase("playermoneyperminute")) {
            return String.valueOf(StorageManager.getPlayerMoneyPerMinute(player));
        }

        // Anchor price
        if(params.equalsIgnoreCase("anchorprice")) {
            return String.valueOf(plugin.getConfig().getDouble("anchor-value"));
        }

        // TOP
        if(params.equalsIgnoreCase("top1")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[0].toString())).getName();
        }
        if(params.equalsIgnoreCase("top2")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[1].toString())).getName();
        }
        if(params.equalsIgnoreCase("top3")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[2].toString())).getName();
        }
        if(params.equalsIgnoreCase("top4")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[3].toString())).getName();
        }
        if(params.equalsIgnoreCase("top5")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[4].toString())).getName();
        }
        if(params.equalsIgnoreCase("top6")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[5].toString())).getName();
        }
        if(params.equalsIgnoreCase("top7")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[6].toString())).getName();
        }
        if(params.equalsIgnoreCase("top8")) {
            return Bukkit.getOfflinePlayer(UUID.fromString(StorageManager.getAnchorTop().keySet().toArray()[7].toString())).getName();
        }

        return null;
    }

    private AnchorSell plugin;
}
