package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.storage.Global;
import mc.nightmarephoenix.anchorsell.storage.StorageManager;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;

import java.util.ArrayList;

public class List extends SubCommands {
    @Override
    public String getDescription() {
        return "Lists all anchors.";
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String syntax() {
        return "/anchor list";
    }

    @Override
    public java.util.List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommand = new ArrayList<>();
        if(sender.hasPermission("anchorsell.admin.list")) {
            for(Player p : Bukkit.getOnlinePlayers()) subcommand.add(p.getName());
        }
        return subcommand;
    }

    @Override
    public String getPermission() {
        return "";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (sender.hasPermission("anchorsell.player.list") && args.length == 1) {
            try {
                StorageManager.getAnchorUserList(Global.plugin, Bukkit.getPlayer(sender.getName()), Bukkit.getPlayer(sender.getName()));
            } catch (InvalidConfigurationException e) {
                sender.sendMessage("An error happened. Contact an administrator.");
            }
        } else if (sender.hasPermission("anchorsell.admin.list") && args.length == 2) {
            try {
                StorageManager.getAnchorUserList(Global.plugin, Bukkit.getOfflinePlayer(args[1]), sender);
            } catch (InvalidConfigurationException e) {
                sender.sendMessage("An error happened. Contact an administrator.");
            }
        }
    }
}
