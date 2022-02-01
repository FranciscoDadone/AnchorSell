package com.franciscodadone.anchorsell.commands.subcommands;

import com.franciscodadone.anchorsell.api.AnchorAPI;
import com.franciscodadone.anchorsell.api.InternalAnchorAPI;
import com.franciscodadone.anchorsell.hooks.Hooks;
import com.franciscodadone.anchorsell.thirdparty.holographicdisplays.HologramMaker;
import com.franciscodadone.anchorsell.utils.Utils;
import com.gmail.filoghost.holographicdisplays.api.Hologram;
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
                InternalAnchorAPI.saveHologram(hologram);
            } else sender.sendMessage(Utils.Color("&cHolographicDisplays and PlaceholderAPI have to be present!"));
        } else Utils.noPermission(getPermission(), sender);
    }
}
