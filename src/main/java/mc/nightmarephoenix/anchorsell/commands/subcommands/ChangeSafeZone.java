package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ChangeSafeZone extends SubCommands {
    @Override
    public String getDescription() {
        return "Changes the safe zone where the anchors can be placed.";
    }

    @Override
    public String getName() {
        return "changeSafeZone";
    }

    @Override
    public String syntax() {
        return "/anchor changeSafeZone [newZone]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.changeSafeZone";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        if (sender.hasPermission("anchorsell.admin.changeSafeZone")) {
            StorageManager.changeSafeZone(Global.plugin, Integer.parseInt(args[1]));
            sender.sendMessage(Utils.Color("&aSafe zone changed to &c" + Integer.parseInt(args[1])));
        }

    }
}
