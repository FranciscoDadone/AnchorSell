package com.franciscodadone.anchorsell.thirdparty.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import com.franciscodadone.anchorsell.hooks.Hooks;
import org.bukkit.Location;

public class RegionManager {
    /**
     * Returns if in the region the user can build.
     * @param location block to check
     * @return if can build
     */
    public boolean canBuild(Location location) {
        if(Hooks.isWorldGuardActive) {
            com.sk89q.worldedit.util.Location wgLocation = BukkitAdapter.adapt(location);
            RegionContainer container = WorldGuard.getInstance().getPlatform().getRegionContainer();
            RegionQuery query = container.createQuery();
            ApplicableRegionSet set = query.getApplicableRegions(wgLocation);
            for (ProtectedRegion region : set) {
                String flags = region.getFlags().toString();
                if(flags.contains("{name='build'}=DENY") || flags.contains("{name='block-place'}=DENY")) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
