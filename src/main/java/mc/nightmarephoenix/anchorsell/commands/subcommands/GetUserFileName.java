package mc.nightmarephoenix.anchorsell.commands.subcommands;

import mc.nightmarephoenix.anchorsell.utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class GetUserFileName extends SubCommands {
    @Override
    public String getDescription() {
        return "Gets the user file name (UUID)";
    }

    @Override
    public String getName() {
        return "getUserFileName";
    }

    @Override
    public String syntax() {
        return "/anchor getUserFileName [username]";
    }

    @Override
    public List<String> getSubCommandsArgs(CommandSender sender, String[] args) {
        ArrayList<String> subcommand = new ArrayList<>();
        for(Player p : Bukkit.getOnlinePlayers()) {
            subcommand.add(p.getName());
        }
        return subcommand;
    }

    @Override
    public String getPermission() {
        return "anchorsell.admin.getUserFileName";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {
        if(sender.hasPermission("anchorsell.admin.getUserFileName")) {
            if(args.length == 2) {
                sender.sendMessage(Utils.Color("&aUser file name: &c" + Bukkit.getPlayer(args[1]).getUniqueId() + ".yml"));
            } else {
                sender.sendMessage(Utils.Color("Usage: &e/anchor getuserfilename [username]"));
            }
        } else Utils.noPermission(getPermission(), sender);
    }
}
