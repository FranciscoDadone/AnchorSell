package com.franciscodadone.anchorsell.api;

import com.franciscodadone.anchorsell.models.Anchor;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.OfflinePlayer;
import org.bukkit.World;

import java.util.Objects;
import java.util.Set;
import java.util.UUID;

public class InternalAnchorAPI {

    /**
     * Saves to RAM (Global) all anchors.
     */
    public static void cacheAllAnchors() {
        GeneralStorage generalData = new GeneralStorage();
        Set<String> a = generalData.getConfig().getKeys(true);

        int x = -1, y = -1, z = -1;
        World world = null;
        int level;
        OfflinePlayer owner = null;
        String ownerName = "";
        for(String str : a) {
            if(StringUtils.countMatches(str, ".") == 1) {
                x = Integer.parseInt(StringUtils.split(str, "all_anchors.")[0]);
                y = Integer.parseInt(StringUtils.split(str, "all_anchors.")[1]);
                z = Integer.parseInt(StringUtils.split(str, "all_anchors.")[2]);
            }
            if(StringUtils.countMatches(str, ".world") == 1) {
                world = Bukkit.getServer().getWorld(Objects.requireNonNull(generalData.getConfig().getString(str)));
            }
            if(StringUtils.countMatches(str, ".owner") == 1) {
                owner = Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(generalData.getConfig().getString(str))));
                PerUserStorage user = new PerUserStorage(Bukkit.getOfflinePlayer(UUID.fromString(Objects.requireNonNull(generalData.getConfig().getString(str)))));

                if(user.getConfig().contains("playerName")) {
                    ownerName = user.getConfig().getString("playerName");
                } else ownerName = "";
            }
            if(StringUtils.countMatches(str, ".level") == 1) {
                level = generalData.getConfig().getInt(str);
                Global.addAnchor(new Anchor(
                        level,
                        new Location(world, x, y, z),
                        owner,
                        ownerName
                ));
            }
        }
    }

    /**
     * Saves the newly created hologram to the database.
     * @param hologram
     */
    public static void saveHologram(Hologram hologram) {
        HologramsStorage storage = new HologramsStorage();
        storage.getConfig().set("hologram", hologram.getLocation());
        storage.saveConfig();
    }

    /**
     * @return Current hologram location.
     */
    public static Location retrieveHologramLocation() {
        HologramsStorage storage = new HologramsStorage();
        return storage.getConfig().getLocation("hologram");
    }
}
