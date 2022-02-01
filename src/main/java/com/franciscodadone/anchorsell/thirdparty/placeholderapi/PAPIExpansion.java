package com.franciscodadone.anchorsell.thirdparty.placeholderapi;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.models.Anchor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.UUID;

public class PAPIExpansion extends PlaceholderExpansion {

    public PAPIExpansion(AnchorSell plugin) {
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
            for(Anchor anchor : AnchorAPI.getAllPlayerAnchors(player)) {
                level += anchor.getLevel();
            }
            return String.valueOf(level);
        }

        // Total player anchors
        if(params.equalsIgnoreCase("playeranchors")) {
            return String.valueOf(AnchorAPI.getPlayerTotalAnchors(player));
        }

        // Money per minute that the player generates via anchors.
        if(params.equalsIgnoreCase("playermoneyperminute")) {
            return String.valueOf(AnchorAPI.getPlayerMoneyPerMinute(player));
        }

        // Anchor price
        if(params.equalsIgnoreCase("anchorprice")) {
            return String.valueOf(plugin.getConfig().getDouble("anchor-value"));
        }

        // TOP
        HashMap top = AnchorAPI.getAnchorTop();
        for(int i = 0; i <= 100; i++) {
            if(params.equalsIgnoreCase("top" + i)) {
                if(top.size() < i) return "";
                return Bukkit.getOfflinePlayer(UUID.fromString(top.keySet().toArray()[top.size() - i].toString())).getName();
            }
        }
        // POINTS
        for(int i = 0; i <= 100; i++) {
            if(params.equalsIgnoreCase("top" + i + "-points")) {
                if(top.size() < i) return "";
                return AnchorAPI.getAnchorTop().values().toArray()[top.size() - i].toString();
            }
        }


        return null;
    }

    private AnchorSell plugin;
}
