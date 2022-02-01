package com.franciscodadone.anchorsell.events.gui;

import com.franciscodadone.anchorsell.AnchorSell;
import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.models.Anchor;
import com.franciscodadone.anchorsell.inventories.ChangeLevelScreen;
import com.franciscodadone.anchorsell.utils.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.data.type.RespawnAnchor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

import java.util.Objects;

public class ChangeLevelInventoryEvents implements Listener {

    public ChangeLevelInventoryEvents(AnchorSell plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent e) {
        if (e == null) return;
        Player p = (Player) e.getWhoClicked();
        Block block = p.getTargetBlock(Utils.getInvisibleBlockList(), 5);
        Location location = block.getLocation();
        Anchor anchor = AnchorAPI.getAnchorFromLoc(location);

        //
        // Checks if the player is in direct contact with the anchor
        //
        if (block.getType() != Material.RESPAWN_ANCHOR) {
            try {
                if(Objects.requireNonNull(Objects.requireNonNull(e.getCurrentItem()).getItemMeta()).getDisplayName().equals(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.upgrades.txt"))))) {
                    p.sendMessage(Utils.Color(Objects.requireNonNull(plugin.getConfig().getString("anchor.cantaccess"))));
                    p.closeInventory();
                    return;
                }
            } catch(Exception ignored) {}
        }

        if (e.getClickedInventory() != null && e.getClickedInventory().getHolder() instanceof ChangeLevelScreen) {
            e.setCancelled(true);

            RespawnAnchor anchorBlock = (RespawnAnchor) block.getBlockData();

            if(e.getCurrentItem().getType().equals(Material.RED_STAINED_GLASS_PANE)) {
                if(anchor.getLevel() > 1) anchor.setLevel(anchor.getLevel() - 1);
            } else if(e.getCurrentItem().getType().equals(Material.LIME_STAINED_GLASS_PANE)) {
                if(anchor.getLevel() < 64) anchor.setLevel(anchor.getLevel() + 1);
            }
            AnchorAPI.changeLevel(anchor, anchor.getLevel());

            anchorBlock.setCharges(Utils.getAnchorCharges(anchor.getLevel()));
            block.setBlockData(anchorBlock);

            p.openInventory(new ChangeLevelScreen(anchor.getLevel()).getInventory());
        }



        }

    private AnchorSell plugin;

}
