package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class ChangeUpgradeMultiplier extends SubCommands {
    @Override
    public String getDescription() {
        return "Changes the upgrade multiplier.";
    }

    @Override
    public String getName() {
        return "changeUpgradeMultiplier";
    }

    @Override
    public String syntax() {
        return "/anchor changeUpgradeMultiplier [newMultiplier]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.changeUpgradeMultiplier";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.changeUpgradeMultiplier") && args.length == 2 && Utils.isNumeric(args[1])) {
            StorageManager.changeUpgradeMultiplier(Global.plugin, Integer.parseInt(args[1]));
            sender.sendMessage(Utils.Color("&aMultiplier changed to &c" + Integer.parseInt(args[1])));
        } else {
            sender.sendMessage(Utils.Color("Usage: &e" + syntax()));
        }
    }
}
