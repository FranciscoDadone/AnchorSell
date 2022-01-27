package mc.nightmarephoenix.anchorsell.thirdparty.holographicdisplays;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Location;

public class HologramMaker {

    public static Hologram createHoloTop(Location location) {
        Hologram hologram = HologramsAPI.createHologram(Global.plugin, location);
        hologram.setAllowPlaceholders(true);

        hologram.appendTextLine(Utils.Color("&7&m----------&r &5&lAnchor TOP &7&m----------"));
        hologram.appendTextLine(Utils.Color("&7#1 &b%anchorsell_top1% &7- &f%anchorsell_top1-points%"));
        hologram.appendTextLine(Utils.Color("&7#2 &b%anchorsell_top2% &7- &f%anchorsell_top2-points%"));
        hologram.appendTextLine(Utils.Color("&7#3 &b%anchorsell_top3% &7- &f%anchorsell_top3-points%"));
        hologram.appendTextLine(Utils.Color("&7&m----------------------------------"));

        return hologram;
    }

}
