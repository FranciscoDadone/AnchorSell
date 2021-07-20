package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.command.CommandSender;

import java.util.ArrayList;
import java.util.List;

public class Reload extends SubCommands {
    @Override
    public String getDescription() {
        return "Reloads the plugin.";
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String syntax() {
        return "/anchor reload";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        return new ArrayList<>();
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.reload";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        Global.plugin.reloadConfig();
        sender.sendMessage(Utils.Color("&aAnchorSell reloaded!"));
    }
}
