package mc.nightmarephoenix.anchorsell.commands;

import mc.nightmarephoenix.anchorsell.AnchorSell;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AnchorCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        boolean isAnchorCommand = label.equalsIgnoreCase("anchor");
        Player player = (Player) sender;

        if (isAnchorCommand && sender instanceof Player) {

            if (sender.hasPermission("anchorsell.player")) {
                player.sendMessage("anchor!!");
            }
        }

        return true;
    }
}