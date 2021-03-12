package mc.nightmarephoenix.anchorsell.worldguard;

import com.sk89q.worldedit.bukkit.BukkitAdapter;
import com.sk89q.worldguard.WorldGuard;
import com.sk89q.worldguard.protection.ApplicableRegionSet;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;
import com.sk89q.worldguard.protection.regions.RegionContainer;
import com.sk89q.worldguard.protection.regions.RegionQuery;
import mc.nightmarephoenix.anchorsell.utils.Global;
import mc.nightmarephoenix.anchorsell.utils.WG;
import org.bukkit.Location;

public class RegionManager {
    public boolean canBuild(Location location) {
        if(Global.getWorldGuard() == WG.ACTIVE) {
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
