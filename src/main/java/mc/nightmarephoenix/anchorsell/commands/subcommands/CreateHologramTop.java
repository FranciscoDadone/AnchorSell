package mc.nightmarephoenix.anchorsell.commands.subcommands;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import mc.nightmarephoenix.anchorsell.api.Global;
import mc.nightmarephoenix.anchorsell.api.StorageManager;
import mc.nightmarephoenix.anchorsell.hooks.Hooks;
import mc.nightmarephoenix.anchorsell.thirdparty.holographicdisplays.HologramMaker;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class CreateHologramTop extends SubCommands {
    @Override
    public String getDescription() {
        return "Creates a hologram of the top.";
    }

    @Override
    public String getName() {
        return "createHologramTop";
    }

    @Override
    public String syntax() {
        return "/anchor createHologramTop";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) { return new ArrayList<>(); }

    @Override
    public String getPermission() {
        return "anchorsell.admin.createholo";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender instanceof Player && sender.hasPermission(getPermission())) {
            if(Hooks.isHolographicDisplaysActive) {
                Hologram hologram = HologramMaker.createHoloTop(((Player)sender).getLocation());
                StorageManager.saveHologram(hologram);
            } else sender.sendMessage(Utils.Color("&cHolographicDisplays and PlaceholderAPI have to be present!"));
        } else Utils.noPermission(getPermission(), sender);
    }
}
