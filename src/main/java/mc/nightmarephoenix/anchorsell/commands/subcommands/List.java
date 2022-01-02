package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.utils.Utils;
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
        return "anchorsell.player.list";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if (args.length == 1) {
            if(sender instanceof Player) {
                if(sender.hasPermission("anchorsell.player.list")) {
                    try {
                        Utils.printAnchorUserList(Bukkit.getPlayer(sender.getName()), sender);
                    } catch (InvalidConfigurationException e) {
                        sender.sendMessage("An error happened. Contact an administrator.");
                    }
                } else Utils.noPermission(getPermission(), sender);
            } else sender.sendMessage("Only players can execute this command.");
        } else if (args.length == 2) {
            if(sender.hasPermission("anchorsell.admin.list")) {
                try {
                    boolean found = Utils.printAnchorUserList(Bukkit.getOfflinePlayer(args[1]), sender);
                    if(!found && !Bukkit.getOnlinePlayers().contains(Bukkit.getOfflinePlayer(args[1]))) {
                        sender.sendMessage(Utils.Color("&cPlayer not found and not online. Make sure to write the name properly (with capitals)."));
                    }
                } catch (InvalidConfigurationException e) {
                    sender.sendMessage("An error happened. Contact an administrator.");
                }
            } else Utils.noPermission("anchorsell.admin.list", sender);
        }
    }
}
